package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.MealEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {
  @Query("SELECT * FROM meals ORDER BY timestamp DESC")
  fun getAllMealsFlow(): Flow<List<MealEntity>>

  @Query("SELECT * FROM meals WHERE dateString = :date ORDER BY timestamp DESC")
  fun getMealsByDateFlow(date: String): Flow<List<MealEntity>>

  @Query("SELECT * FROM meals WHERE dateString IN (:dates) ORDER BY timestamp DESC")
  fun getMealsForDatesFlow(dates: List<String>): Flow<List<MealEntity>>

  @Query("SELECT COALESCE(SUM(calories), 0) FROM meals WHERE dateString = :date")
  fun getTotalCaloriesForDateFlow(date: String): Flow<Int>

  @Query("SELECT COALESCE(SUM(calories), 0) FROM meals WHERE dateString IN (:dates)")
  fun getTotalCaloriesForDatesFlow(dates: List<String>): Flow<Int>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertMeal(meal: MealEntity): Long

  @Query("DELETE FROM meals WHERE id = :id")
  suspend fun deleteMealById(id: Long)

  @Query("DELETE FROM meals")
  suspend fun deleteAllMeals()
}
