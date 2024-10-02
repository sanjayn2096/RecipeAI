package com.sunj.recipeai.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users_table")
    suspend fun getAllUsers(): List<User>

    @Query("SELECT * FROM users_table WHERE user_id = :id")
    suspend fun getUserFilterById(id:Int) : List<UserFilter>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserFilter(userFilter: UserFilter)

    @Query("SELECT user_mood FROM user_filters_table WHERE user_id = :id")
    suspend fun getMood(id: Int) : String

    @Query("SELECT dietary_restriction FROM user_filters_table WHERE user_id = :id")
    suspend fun getDietaryRestrictions(id: Int) : List<String>

    @Query("SELECT cooking_preferences FROM user_filters_table WHERE user_id = :id")
    suspend fun getCookingPreferences(id: Int) : List<String>

    @Query("SELECT cuisine_preferences FROM user_filters_table WHERE user_id = :id")
    suspend fun getCuisinePreferences(id: Int) : List<String>

}