package com.example.data

import android.content.Context
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class AppThemeColor(
    val title: String,
    val primaryColor: Color,
    val primaryDarkColor: Color,
    val containerColor: Color,
    val hexCode: String
) {
    BLUE("Azul Confiança", Color(0xFF2563EB), Color(0xFF1D4ED8), Color(0xFFEFF6FF), "#2563EB"),
    GREEN("Verde Calmo", Color(0xFF059669), Color(0xFF047857), Color(0xFFECFDF5), "#059669"),
    PURPLE("Violeta Suave", Color(0xFF7C3AED), Color(0xFF6D28D9), Color(0xFFF5F3FF), "#7C3AED"),
    ORANGE("Laranja Alegria", Color(0xFFEA580C), Color(0xFFC2410C), Color(0xFFFFF7ED), "#EA580C"),
    PINK("Rosa Acolhedor", Color(0xFFDB2777), Color(0xFFBE185D), Color(0xFFFDF2F8), "#DB2777"),
    TEAL("Menta Sereno", Color(0xFF0D9488), Color(0xFF0F766E), Color(0xFFF0FDF4), "#0D9488")
}

data class UserProfile(
    val name: String = "Lucas",
    val nickname: String = "Lulu",
    val avatarEmoji: String = "👦",
    val themeColorKey: String = AppThemeColor.BLUE.name,
    val speechPitch: Float = 1.2f, // Default cheerful child pitch
    val speechRate: Float = 0.9f,  // Default calm rate
    val useGreetingInCards: Boolean = true
) {
    val themeColor: AppThemeColor
        get() = try {
            AppThemeColor.valueOf(themeColorKey)
        } catch (e: Exception) {
            AppThemeColor.BLUE
        }

    val displayName: String
        get() = nickname.ifBlank { name.ifBlank { "Criança" } }
}

class UserProfileRepository(context: Context) {
    private val prefs = context.getSharedPreferences("user_profile_prefs", Context.MODE_PRIVATE)

    private val _userProfile = MutableStateFlow(loadProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private fun loadProfile(): UserProfile {
        return UserProfile(
            name = prefs.getString("name", "Lucas") ?: "Lucas",
            nickname = prefs.getString("nickname", "Lulu") ?: "Lulu",
            avatarEmoji = prefs.getString("avatarEmoji", "👦") ?: "👦",
            themeColorKey = prefs.getString("themeColorKey", AppThemeColor.BLUE.name) ?: AppThemeColor.BLUE.name,
            speechPitch = prefs.getFloat("speechPitch", 1.2f),
            speechRate = prefs.getFloat("speechRate", 0.9f),
            useGreetingInCards = prefs.getBoolean("useGreetingInCards", true)
        )
    }

    fun saveProfile(profile: UserProfile) {
        prefs.edit()
            .putString("name", profile.name)
            .putString("nickname", profile.nickname)
            .putString("avatarEmoji", profile.avatarEmoji)
            .putString("themeColorKey", profile.themeColorKey)
            .putFloat("speechPitch", profile.speechPitch)
            .putFloat("speechRate", profile.speechRate)
            .putBoolean("useGreetingInCards", profile.useGreetingInCards)
            .apply()

        _userProfile.value = profile
    }
}
