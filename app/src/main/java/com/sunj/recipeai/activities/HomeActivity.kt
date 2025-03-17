package com.sunj.recipeai.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.sunj.recipeai.SessionManager
import com.sunj.recipeai.model.UserData
import com.sunj.recipeai.screens.HomeScreen
import com.sunj.recipeai.viewmodel.HomeViewModel
import com.sunj.recipeai.viewmodel.RecipeViewModel
import com.sunj.recipeai.viewmodel.ViewModelFactory

class HomeActivity : AppCompatActivity() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var recipeViewModel: RecipeViewModel
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        Log.d("HomeActivity", "onCreate() called")
        homeViewModel = ViewModelProvider(
            this, ViewModelFactory(applicationContext)
        )[HomeViewModel::class.java]

        recipeViewModel = ViewModelProvider(
            this, ViewModelFactory(applicationContext)
        )[RecipeViewModel::class.java]

        homeViewModel.getUserDetails()

        sessionManager = SessionManager(this)

        homeViewModel.userLiveData.observe(this) {
            Log.d("HomeActivity", "user live data changed" + it?.favorite_recipes)
            setContent {
                RecipeAIApp(it)
            }
        }
    }

    @Composable
    fun RecipeAIApp(userData: UserData?) {
        val navController = rememberNavController()
        HomeScreen(navController, userData, homeViewModel, recipeViewModel, sessionManager)
    }

}
