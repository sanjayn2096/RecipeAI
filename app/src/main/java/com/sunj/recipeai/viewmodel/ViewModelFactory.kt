package com.sunj.recipeai.viewmodel

import android.content.Context
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.sunj.recipeai.database.UserDatabase

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
            val sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
            val apiKey = context.packageManager
                .getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
                .metaData.getString("com.sunj.recipeai.API_KEY")
            return RecipeViewModel(apiKey ?: "", sharedPreferences) as T
        }

        if(modelClass.isAssignableFrom(LoginViewModel::class.java)){
            val userDb = Room.databaseBuilder(
                context,
                UserDatabase::class.java, "user-database"
            ).fallbackToDestructiveMigration().build()
            return LoginViewModel(userDb) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
