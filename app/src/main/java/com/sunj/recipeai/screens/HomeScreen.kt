package com.sunj.recipeai.screens

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.core.content.ContextCompat
import androidx.navigation.compose.*
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.sunj.recipeai.BackButton
import com.sunj.recipeai.R
import com.sunj.recipeai.activities.LoginActivity
import com.sunj.recipeai.model.UserData
import com.sunj.recipeai.themes.RecipeTheme
import com.sunj.recipeai.viewmodel.HomeViewModel
import com.sunj.recipeai.viewmodel.RecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController,
               userData: UserData?,
               homeViewModel: HomeViewModel,
               recipeViewModel: RecipeViewModel
) {
    val selectedTab = remember { mutableStateOf(BottomNavItem.Home) }
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val showBars = currentBackStackEntry?.destination?.route == "home"

    val isSignedOut by homeViewModel.isSignedOut.observeAsState()
    val isBeingEdited by recipeViewModel.isBeingEdited.observeAsState()

    // Redirect to login if user is signed out
    LaunchedEffect(isSignedOut) {
        Log.d("HomeScreen", "Signed Out Value = $isSignedOut")
        if (isSignedOut == true) {
            val context = navController.context
            val intent = Intent(context, LoginActivity::class.java)
            Log.d("HomeScreen", "Redirecting to login")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }

    LaunchedEffect(isBeingEdited) {
        Log.d("HomeScreen", "Recipe Being Edited = $isBeingEdited")
        if (isBeingEdited == true) {
           navController.navigate(GeneratorScreen.Mood.route)
        }
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    RecipeTheme {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                if (showBars) HomeScreenTopBar(navController, homeViewModel)
            },
            bottomBar = {
                if (showBars) BottomNavigationBar(selectedTab, navController)
            }
        ) { innerPadding ->
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                NavigationGraph(navController, userData)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenTopBar(navController: NavController, homeViewModel: HomeViewModel) {
    val menuExpanded = remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        title = {
            Text("Home",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            Box {
                IconButton(onClick = { menuExpanded.value = true }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert, contentDescription = "Menu",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                DropdownMenu(
                    expanded = menuExpanded.value,
                    onDismissRequest = { menuExpanded.value = false }
                ) {
                    DropdownMenuItem(text = { Text("Help") }, onClick = { /* TODO: Handle Help */ })
                    DropdownMenuItem(text = { Text("Tutorial") }, onClick = { /* TODO: Handle Tutorial */ })
                    DropdownMenuItem(text = { Text("Tips") }, onClick = { /* TODO: Handle Tips */ })
                    DropdownMenuItem(text = { Text("Logout") }, onClick = { homeViewModel.signOut() })
                }
            }
        },
        actions = {
            IconButton(onClick = { navController.navigate("profile") }) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle, contentDescription = "Profile",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    )
}

@Composable
fun BottomNavigationBar(selectedTab: MutableState<BottomNavItem>, navController: NavHostController) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        BottomNavItem.entries.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(item.icon,
                        contentDescription = item.label,
                        tint = colorResource(item.color),
                    )
                       },
                label = { Text(item.label) },
                selected = selectedTab.value == item,
                onClick = {
                    selectedTab.value = item
                    when (item) {
                        BottomNavItem.Home -> navController.navigate("home")
                        BottomNavItem.AI -> navController.navigate("mood")
                        BottomNavItem.Favorites -> navController.navigate("favorites")
                    }
                }
            )
        }
    }
}

enum class BottomNavItem(val label: String, val icon: ImageVector, val color: Int) {
    Home("Home", Icons.Filled.Home, R.color.purple),
    AI("Create Recipes", Icons.Filled.AutoAwesome, R.color.yellow),
    Favorites("Favorites", Icons.Filled.Favorite, R.color.red)
}

@Composable
fun NavigationGraph(navController: NavHostController, userData: UserData?) {
    NavHost(navController, startDestination = "home") {
        // Home does not have a back button
        composable("home") { HomeContent() }
        composable(GeneratorScreen.Mood.route) { PromptScreen(navController, GeneratorScreen.Mood.route) }
        composable(GeneratorScreen.DietRestrictions.route) { PromptScreen(navController, GeneratorScreen.DietRestrictions.route) }
        composable(GeneratorScreen.CuisinePreference.route) { PromptScreen(navController, GeneratorScreen.CuisinePreference.route)}
        composable(GeneratorScreen.CookingPreferences.route) { PromptScreen(navController, GeneratorScreen.CookingPreferences.route)}
        composable(GeneratorScreen.RecipeActivity.route) { RecipeActivityScreen(navController, userData) }

        // Profile and Favorites have a back button
        composable("profile") { ScreenWithBackButton(navController, "User's Profile", { ProfileScreen(navController, userData) }) }
        composable("favorites") { ScreenWithBackButton(navController,"User's Favorites", { FavoriteRecipesScreen(navController, userData) }) }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenWithBackButton(
    navController: NavHostController,
    title: String?,
    screenContent: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { title?.let { Text(it, style = MaterialTheme.typography.headlineMedium) } },
                navigationIcon = {
                    BackButton(
                        onClick = {navController.popBackStack()},
                        modifier = Modifier.size(30.dp)
                    )
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            screenContent()
        }
    }
}

@Composable
fun HomeContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(id = R.drawable.bg_regular), // Your drawable
                contentScale = ContentScale.Crop // Crop to fit screen
            )
            .padding(16.dp)
    ) {
        Text(
            text = "Welcome to RecipeAI, Your space to create your own recipes.",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp))
    }

}


@Composable
fun ProfileScreen(navController: NavController, userData: UserData?) {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        //Text("User Profile", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Icon(Icons.Filled.AccountCircle, contentDescription = "Profile", modifier = Modifier.size(128.dp))
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = userData?.firstName ?: "John Doe", onValueChange = {}, label = { Text("First Name") })
        OutlinedTextField(value = userData?.email ?: "john@example.com", onValueChange = {}, label = { Text("Email") })
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /* Save User Data */ }) {
            Text("Save")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text("Back")
        }
    }
}

@Composable
fun FavoriteRecipesScreen(navController: NavController, userData: UserData?) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        //Text("Favorite Recipes", style = MaterialTheme.typography.headlineMedium)
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
                                .padding(8.dp),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.custom_green))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    "${userData.favorite_recipes[it].recipeName}",
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

@Preview(showBackground = true)
    @Composable
    fun PreviewTopBar() {
        //HomeScreenTopBar()
    }
