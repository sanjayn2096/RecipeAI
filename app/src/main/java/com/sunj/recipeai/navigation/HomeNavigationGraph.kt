package com.sunj.recipeai.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.sunj.recipeai.SessionManager
import com.sunj.recipeai.model.Recipes
import com.sunj.recipeai.model.UserData
import com.sunj.recipeai.screens.FavoriteRecipesScreen
import com.sunj.recipeai.screens.GeneratorScreen
import com.sunj.recipeai.screens.HomeContent
import com.sunj.recipeai.screens.ProfileScreen
import com.sunj.recipeai.screens.PromptScreen
import com.sunj.recipeai.screens.RecipeActivityScreen
import com.sunj.recipeai.screens.ScreenWithBackButton
import com.sunj.recipeai.screens.ShowRecipe

@Composable
fun HomeNavigationGraph(navController: NavHostController, userData: UserData?, sessionManager: SessionManager) {
    NavHost(navController, startDestination = "home") {
        // Home does not have a back button
        composable("home") { HomeContent(navController, sessionManager) }
        composable(GeneratorScreen.Mood.route) { PromptScreen(navController, GeneratorScreen.Mood.route) }
        composable(GeneratorScreen.DietRestrictions.route) { PromptScreen(navController, GeneratorScreen.DietRestrictions.route) }
        composable(GeneratorScreen.CuisinePreference.route) { PromptScreen(navController, GeneratorScreen.CuisinePreference.route) }
        composable(GeneratorScreen.CookingPreferences.route) { PromptScreen(navController, GeneratorScreen.CookingPreferences.route) }
        composable(GeneratorScreen.RecipeActivity.route) { RecipeActivityScreen(navController, userData) }

        // Profile and Favorites have a back button
        composable("profile") { ScreenWithBackButton(navController, "User's Profile", { ProfileScreen(navController, userData) }) }
        composable("favorites") { ScreenWithBackButton(navController,"User's Favorites", { FavoriteRecipesScreen(navController, userData) }) }
        composable(
            "showRecipeScreen/{recipeJson}",
            arguments = listOf(navArgument("recipeJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val recipeJson = backStackEntry.arguments?.getString("recipeJson")
            val recipe = Gson().fromJson(recipeJson, Recipes::class.java)
            ShowRecipe(recipe) { navController.popBackStack() }
        }
    }
}