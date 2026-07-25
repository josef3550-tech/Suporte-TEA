package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.CommunicationCard
import com.example.data.RoutineItem
import com.example.data.RoutineRepository
import com.example.data.UserProfile
import com.example.data.UserProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RoutineViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: RoutineRepository
    private val userProfileRepository: UserProfileRepository = UserProfileRepository(application)

    val uiState: StateFlow<List<RoutineItem>>
    val userProfile: StateFlow<UserProfile> = userProfileRepository.userProfile

    private val _lastSpokenCard = MutableStateFlow<CommunicationCard?>(null)
    val lastSpokenCard: StateFlow<CommunicationCard?> = _lastSpokenCard.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = RoutineRepository(database.routineDao())

        // Ensure default items exist on startup
        viewModelScope.launch {
            repository.ensureDefaultItems()
        }

        uiState = repository.allItems.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    fun speakCard(card: CommunicationCard, onSpeak: (String, Float, Float) -> Unit) {
        _lastSpokenCard.value = card
        val profile = userProfile.value
        onSpeak(card.text, profile.speechPitch, profile.speechRate)
    }

    fun speakCard(card: CommunicationCard, onSpeak: (String) -> Unit) {
        _lastSpokenCard.value = card
        onSpeak(card.text)
    }

    fun updateProfile(profile: UserProfile) {
        userProfileRepository.saveProfile(profile)
    }

    fun testVoice(onSpeak: (String, Float, Float) -> Unit) {
        val profile = userProfile.value
        val nameToUse = profile.displayName
        val sampleText = "Olá! Meu nome é $nameToUse. Esta é a minha voz no aplicativo Suporte T E A!"
        onSpeak(sampleText, profile.speechPitch, profile.speechRate)
    }

    fun clearLastSpoken() {
        _lastSpokenCard.value = null
    }

    fun toggleItemCompleted(item: RoutineItem) {
        viewModelScope.launch {
            repository.update(item.copy(isCompleted = !item.isCompleted))
        }
    }

    fun toggleItemEnabled(item: RoutineItem) {
        viewModelScope.launch {
            repository.update(item.copy(isEnabled = !item.isEnabled))
        }
    }

    fun deleteItem(item: RoutineItem) {
        viewModelScope.launch {
            repository.deleteById(item.id)
        }
    }

    fun resetAllCompletions() {
        viewModelScope.launch {
            repository.resetAllCompletion()
        }
    }

    fun addNewItem(title: String, timeText: String, emoji: String) {
        viewModelScope.launch {
            val items = uiState.value
            val maxOrder = items.maxOfOrNull { it.orderIndex } ?: -1
            val newItem = RoutineItem(
                title = title,
                timeText = timeText,
                emoji = emoji,
                orderIndex = maxOrder + 1,
                isCompleted = false,
                isEnabled = true
            )
            repository.insert(newItem)
        }
    }

    fun updateItem(item: RoutineItem, title: String, timeText: String, emoji: String) {
        viewModelScope.launch {
            val updated = item.copy(
                title = title,
                timeText = timeText,
                emoji = emoji
            )
            repository.update(updated)
        }
    }

    fun moveItemUp(item: RoutineItem) {
        viewModelScope.launch {
            val items = uiState.value
            val index = items.indexOfFirst { it.id == item.id }
            if (index > 0) {
                val previousItem = items[index - 1]
                
                // Swap orderIndex
                val tempOrder = item.orderIndex
                val updatedItem = item.copy(orderIndex = previousItem.orderIndex)
                val updatedPrevious = previousItem.copy(orderIndex = tempOrder)
                
                repository.update(updatedItem)
                repository.update(updatedPrevious)
            }
        }
    }

    fun moveItemDown(item: RoutineItem) {
        viewModelScope.launch {
            val items = uiState.value
            val index = items.indexOfFirst { it.id == item.id }
            if (index != -1 && index < items.lastIndex) {
                val nextItem = items[index + 1]
                
                // Swap orderIndex
                val tempOrder = item.orderIndex
                val updatedItem = item.copy(orderIndex = nextItem.orderIndex)
                val updatedNext = nextItem.copy(orderIndex = tempOrder)
                
                repository.update(updatedItem)
                repository.update(updatedNext)
            }
        }
    }
}
