package com.sunj.recipeai.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users_table")
    suspend fun getAllUsers(): List<User>

    @Query("SELECT * FROM users_table WHERE user_name = :userName AND user_password = :userPassword")
    suspend fun getUser(userName: String, userPassword: String): User

    @Query("SELECT * FROM users_table WHERE user_name = :userName")
    suspend fun getUserByUsername(userName: String): User

}