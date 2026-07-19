package com.example.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "routine_items")
data class RoutineItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val timeText: String,
    val emoji: String,
    val isCompleted: Boolean = false,
    val isEnabled: Boolean = true,
    val orderIndex: Int
)

@Dao
interface RoutineDao {
    @Query("SELECT * FROM routine_items ORDER BY orderIndex ASC")
    fun getAllItems(): Flow<List<RoutineItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: RoutineItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<RoutineItem>)

    @Update
    suspend fun updateItem(item: RoutineItem)

    @Query("DELETE FROM routine_items WHERE id = :id")
    suspend fun deleteItemById(id: Int)

    @Query("SELECT COUNT(*) FROM routine_items")
    suspend fun getCount(): Int

    @Query("UPDATE routine_items SET isCompleted = 0")
    suspend fun resetAllCompletion()
}

@Database(entities = [RoutineItem::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun routineDao(): RoutineDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "suporte_tea_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class RoutineRepository(private val routineDao: RoutineDao) {
    val allItems: Flow<List<RoutineItem>> = routineDao.getAllItems()

    suspend fun insert(item: RoutineItem) {
        routineDao.insertItem(item)
    }

    suspend fun insertAll(items: List<RoutineItem>) {
        routineDao.insertItems(items)
    }

    suspend fun update(item: RoutineItem) {
        routineDao.updateItem(item)
    }

    suspend fun deleteById(id: Int) {
        routineDao.deleteItemById(id)
    }

    suspend fun resetAllCompletion() {
        routineDao.resetAllCompletion()
    }

    suspend fun ensureDefaultItems() {
        val count = routineDao.getCount()
        if (count == 0) {
            val defaultItems = listOf(
                RoutineItem(title = "Acordar", timeText = "07:30", emoji = "⏰", orderIndex = 0),
                RoutineItem(title = "Escovar os dentes", timeText = "07:45", emoji = "🪥", orderIndex = 1),
                RoutineItem(title = "Tomar banho", timeText = "08:00", emoji = "🚿", orderIndex = 2),
                RoutineItem(title = "Café da manhã", timeText = "08:15", emoji = "🍳", orderIndex = 3),
                RoutineItem(title = "Escola", timeText = "08:45", emoji = "🏫", orderIndex = 4),
                RoutineItem(title = "Almoço", timeText = "12:30", emoji = "🍲", orderIndex = 5),
                RoutineItem(title = "Descanso", timeText = "13:30", emoji = "💤", orderIndex = 6),
                RoutineItem(title = "Terapia", timeText = "15:00", emoji = "🧠", orderIndex = 7),
                RoutineItem(title = "Brincar", timeText = "16:30", emoji = "🧸", orderIndex = 8),
                RoutineItem(title = "Jantar", timeText = "19:00", emoji = "🍕", orderIndex = 9),
                RoutineItem(title = "Dormir", timeText = "21:00", emoji = "🌙", orderIndex = 10)
            )
            routineDao.insertItems(defaultItems)
        }
    }
}
