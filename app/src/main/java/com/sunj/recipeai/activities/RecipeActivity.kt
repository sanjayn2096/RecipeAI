package com.sunj.recipeai.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.sunj.recipeai.SessionManager
import com.sunj.recipeai.model.UserData
import com.sunj.recipeai.navigation.RecipeScreenNavigationGraph
import com.sunj.recipeai.themes.RecipeTheme
import com.sunj.recipeai.viewmodel.RecipeViewModel
import com.sunj.recipeai.viewmodel.ViewModelFactory

class RecipeActivity : ComponentActivity() {
    private val recipeViewModel: RecipeViewModel by viewModels { ViewModelFactory(applicationContext) }
    private lateinit var sessionManager: SessionManager
    private lateinit var userData: UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("RecipeActivity", "onCreate() called")

        val userDataJson = intent.getStringExtra("userData")
        userData = Gson().fromJson(userDataJson, UserData::class.java)

        sessionManager = SessionManager(this)

        setContent {
            RecipeTheme {
                val navController = rememberNavController()
                RecipeScreenNavigationGraph(navController, recipeViewModel, userData, sessionManager, lifecycleScope)
            }
        }
    }
}

