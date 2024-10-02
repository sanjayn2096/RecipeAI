package com.sunj.recipeai.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [User::class], [UserFilter::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDAO
}