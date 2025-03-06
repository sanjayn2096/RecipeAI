package com.sunj.recipeai.screens

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.gson.Gson
import com.sunj.recipeai.R
import com.sunj.recipeai.SessionManager
import com.sunj.recipeai.activities.HomeActivity
import com.sunj.recipeai.activities.RecipeActivity
import com.sunj.recipeai.model.Recipes
import com.sunj.recipeai.model.UserData
import com.sunj.recipeai.model.optionMapping
import com.sunj.recipeai.ui.BackButton
import com.sunj.recipeai.viewmodel.RecipeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RecipeScreen(
    navController: NavHostController,
    recipeViewModel: RecipeViewModel,
    userData: UserData,
    activity: RecipeActivity,
    sessionManager: SessionManager,
    lifecycleScope: CoroutineScope
) {
    val recipes by recipeViewModel.recipe.observeAsState(emptyList())
    val isLoading by recipeViewModel.isLoading.observeAsState(true)
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.cooking_animation))
    //sessionManager.getUserFavorites
    // In case user refreshed, we are not getting latest User Data, we are preloading whatever was passed in the intent.
    val favoriteRecipesNames = userData.favorite_recipes!!.map { it.recipeName } as MutableList

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (isLoading) {
            recipeViewModel.callFetchRecipes()
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    LottieAnimation(
                        composition,
                        iterations = LottieConstants.IterateForever,
                        modifier = Modifier.size(200.dp)
                    )
                    Text(
                        "Sending some tasty recipes your way...",
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        } else {
            BackButton(
                onClick = {
                    val context = navController.context
                    context.startActivity(Intent(context, HomeActivity::class.java))
                    activity.finish()
                },
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.weight(5f)) {
                items(recipes) { recipe ->
                    RecipeItem(
                        recipe = recipe,
                        onClick = {
                            val recipeJson = Uri.encode(Gson().toJson(recipe))
                            navController.navigate("showRecipeScreen/$recipeJson")
                        },
                        recipeViewModel,
                        sessionManager,
                        favoriteRecipesNames,
                        lifecycleScope
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth().weight(0.5f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                        recipeViewModel.isBeingEdited()
                        navController.navigate("editPreferenceScreen")
                    }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Preferences"
                    )
                }
                IconButton(onClick = { recipeViewModel.callFetchRecipes() }) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh Recipes"
                    )
                }
            }
        }
    }
}

@Composable
fun RecipeItem(
    recipe: Recipes,
    onClick: () -> Unit,
    recipeViewModel: RecipeViewModel,
    sessionManager: SessionManager,
    favoriteRecipesNames: MutableList<String>,
    lifecycleScope: CoroutineScope
) {
    var isFavorited by rememberSaveable { mutableStateOf(favoriteRecipesNames.contains(recipe.recipeName)) }
    var favoriteJob: Job? = null
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary)
    ) {
        Row(modifier = Modifier.padding(8.dp).fillMaxWidth().height(150.dp)) {
            Image(
                modifier = Modifier.weight(1f).wrapContentWidth().wrapContentHeight()
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.FillBounds,
                painter = painterResource(id = R.drawable.food_pic),
                contentDescription = "Recipe Image"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(2f).height(150.dp)) {
                Text(
                    text = recipe.recipeName,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    maxLines = 2
                )
                Text(
                    text = recipe.cuisine,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
                Text(
                    text = "Cooking Time: ${recipe.cookingTime}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
            }
            IconButton(
                onClick = {
                    isFavorited = !isFavorited
                    favoriteRecipesNames.add(recipe.recipeName)
                    recipe.isFavorite = isFavorited
                    favoriteJob?.cancel()
                    // Delay API call by 500ms to debounce
                    favoriteJob = lifecycleScope.launch {
                        delay(2000)  // Wait for 2000ms before making API call
                        recipeViewModel.modifyFavoriteRecipe(
                            sessionManager.getUserId()!!,
                            recipe
                        )
                    }
                },
                modifier = Modifier.align(Alignment.Bottom)
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Favorite Recipe",
                    tint = if (isFavorited) Color.Red else Color.Gray
                )
            }
        }
    }
}

@Composable
fun EditPreferenceScreen(navController: NavHostController, recipeViewModel: RecipeViewModel) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    val preferences = listOf(
        GeneratorScreen.Mood.route,
        GeneratorScreen.CuisinePreference.route,
        GeneratorScreen.DietRestrictions.route,
        GeneratorScreen.CookingPreferences.route,
    )

    val customInput = remember { mutableStateMapOf<String, String>() }

    Box(
        modifier = Modifier.paint(
            painter = painterResource(id = R.drawable.bg_regular),
            contentScale = ContentScale.Crop
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BackButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.Start)
            )

            Text(
                text = "Edit Your Preferences",
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.source_sans_pro)),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            preferences.forEach { preference ->
                val options = optionMapping[preference]?.map { stringResource(it) }
                    ?: emptyList()
                Log.d("EditPreferenceScreen", "options = $options")
                var expanded by remember { mutableStateOf(false) }
                var selectedOption by remember {
                    mutableStateOf(
                        sessionManager.getPreference(
                            preference
                        ) ?: ""
                    )
                }
                var showCustomInput by remember { mutableStateOf(false) }

                Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Text(text = preference, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(4.dp))

                    Box(
                        modifier = Modifier.fillMaxWidth()
                            .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable { expanded = true },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = selectedOption.ifEmpty { "Select an option" },
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = Icons.Filled.ArrowDropDown,
                                contentDescription = "Dropdown"
                            )
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            options.forEach { option ->
                                Log.d("EditPreferenceScreen", "option = " + option)
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        sessionManager.savePreference(preference, option)
                                        selectedOption = option
                                        expanded = false
                                        showCustomInput = option == "Other"
                                    }
                                )
                            }
                        }
                    }

                    if (showCustomInput) {
                        TextField(
                            value = customInput[preference] ?: "",
                            onValueChange = { customInput[preference] = it },
                            placeholder = { Text("Enter custom preference") },
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                        )
                    }
                }
            }

            Button(
                onClick = {
                    navController.popBackStack()
                    recipeViewModel.callFetchRecipes()
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Set Preferences and Regenerate")
            }
        }
    }
}