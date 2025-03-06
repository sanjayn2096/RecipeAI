package com.sunj.recipeai.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.sunj.recipeai.SessionManager
import com.sunj.recipeai.activities.RecipeActivity
import com.sunj.recipeai.model.Recipes
import com.sunj.recipeai.model.UserData
import com.sunj.recipeai.screens.EditPreferenceScreen
import com.sunj.recipeai.screens.RecipeScreen
import com.sunj.recipeai.screens.ShowRecipe
import com.sunj.recipeai.viewmodel.RecipeViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun RecipeScreenNavigationGraph(
    navHostController: NavHostController,
    viewModel: RecipeViewModel,
    userData: UserData,
    sessionManager: SessionManager,
    lifecycleScope: CoroutineScope
) {
    NavHost(navHostController, startDestination = "recipeScreen") {
        // recipeScreen does not have a back button
        composable("recipeScreen") {
            RecipeScreen(
                navHostController,
                viewModel,
                userData,
                RecipeActivity(),
                sessionManager,
                lifecycleScope
            )
        }
        composable("editPreferenceScreen") { EditPreferenceScreen(navHostController, viewModel) }
        composable(
            "showRecipeScreen/{recipeJson}",
            arguments = listOf(navArgument("recipeJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val recipeJson = backStackEntry.arguments?.getString("recipeJson")
            val recipe = Gson().fromJson(recipeJson, Recipes::class.java)
            ShowRecipe(recipe) { navHostController.popBackStack() }
        }
    }
}