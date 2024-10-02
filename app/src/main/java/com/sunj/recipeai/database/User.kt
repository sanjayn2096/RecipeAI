package com.sunj.recipeai.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "users_table")
data class User (
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    val userId: Int,
    @ColumnInfo(name = "user_name")
    val name: String,
    @ColumnInfo(name = "user_email")
    val email: String,
    @ColumnInfo(name = "user_password")
    val password: String,
    @ColumnInfo(name = "user_filter")
    val filter: UserFilter
)

@Entity(
    tableName = "user_filters_table",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("user_id"),
            childColumns = arrayOf("user_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )]
)
data class UserFilter (
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    val userId : Int,
    @ColumnInfo(name = "user_mood")
    val mood: String,
    @ColumnInfo(name = "dietary_restriction")
    val dietaryRestriction: List<String>,
    @ColumnInfo(name = "cooking_preferences")
    val cookingPreferences: List<String>,
    @ColumnInfo(name = "cuisine_preferences")
    val cuisinePreferences: List<String>
)