package com.sunj.recipeai.screens

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.gson.Gson
import com.sunj.recipeai.R
import com.sunj.recipeai.model.UserData

@Composable
fun FavoriteRecipesScreen(navController: NavController, userData: UserData?) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (userData?.favorite_recipes == null) {
            Log.d("FavoriteRecipesScreen", "No favorite recipes")
            Text("No Favorite Recipes yet, Try Generating your own recipes by clicking below.")
            IconButton(
                modifier = Modifier.align(Alignment.CenterHorizontally).size(48.dp),
                onClick = { navController.navigate(GeneratorScreen.Mood.route)}) {
                Icon(Icons.Filled.AutoAwesome, contentDescription = "Back")
            }
        } else {
            LazyColumn {
                items(userData.favorite_recipes.size) {
                    if (userData.favorite_recipes[it].isFavorite) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .padding(8.dp),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.custom_green)),
                            onClick = {
                                val recipeJson = Uri.encode(Gson().toJson(userData.favorite_recipes[it]))
                                navController.navigate("showRecipeScreen/$recipeJson")
                            }
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    userData.favorite_recipes[it].recipeName,
                                    style = MaterialTheme.typography.headlineSmall
                                )
                                Text(
                                    "Cuisine ${userData.favorite_recipes[it].cuisine}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    "Cooking Time ${userData.favorite_recipes[it].cookingTime}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}