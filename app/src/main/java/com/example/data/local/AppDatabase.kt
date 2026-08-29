package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.MealDao
import com.example.data.local.dao.SettingsDao
import com.example.data.local.dao.UserProfileDao
import com.example.data.local.entity.AppSettingsEntity
import com.example.data.local.entity.MealEntity
import com.example.data.local.entity.UserProfileEntity

@Database(
  entities = [UserProfileEntity::class, MealEntity::class, AppSettingsEntity::class],
  version = 2,
  exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
  abstract fun userProfileDao(): UserProfileDao
  abstract fun mealDao(): MealDao
  abstract fun settingsDao(): SettingsDao

  companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          AppDatabase::class.java,
          "tmb_metabolic_tracker.db"
        ).fallbackToDestructiveMigration().build()
        INSTANCE = instance
        instance
      }
    }
  }
}
