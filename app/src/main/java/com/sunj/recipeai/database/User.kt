package com.sunj.recipeai.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users_table")
data class User (
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    val userId: String,
    @ColumnInfo(name = "user_username")
    val username: String,
    @ColumnInfo(name = "user_first_name")
    val firstName: String,
    @ColumnInfo(name = "user_last_name")
    val lastName: String,
    @ColumnInfo(name = "user_email")
    val email: String,
    @ColumnInfo(name = "user_password")
    val password: String
)