package com.sunj.recipeai.screens

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.*
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.sunj.recipeai.R
import com.sunj.recipeai.SessionManager
import com.sunj.recipeai.activities.LoginActivity
import com.sunj.recipeai.model.UserData
import com.sunj.recipeai.navigation.HomeNavigationGraph
import com.sunj.recipeai.themes.RecipeTheme
import com.sunj.recipeai.viewmodel.HomeViewModel
import com.sunj.recipeai.viewmodel.RecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController,
               userData: UserData?,
               homeViewModel: HomeViewModel,
               recipeViewModel: RecipeViewModel,
               sessionManager: SessionManager
) {
    val selectedTab = remember { mutableStateOf(BottomNavItem.Home) }
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val showBars = currentBackStackEntry?.destination?.route == "home"

    val isSignedOut by homeViewModel.isSignedOut.observeAsState()
    val isBeingEdited by recipeViewModel.isBeingEdited.observeAsState()

    // Redirect to login if user is signed out

    if (isSignedOut == true) {
        val context = navController.context
        val intent = Intent(context, LoginActivity::class.java)
        Log.d("HomeScreen", "Redirecting to login")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
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
                /*if (showBars)*/ HomeScreenTopBar(navController, homeViewModel, selectedTab)
            },
            bottomBar = {
                /*if (showBars)*/ BottomNavigationBar(selectedTab, navController)
            }
        ) { innerPadding ->
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                HomeNavigationGraph(navController, userData, sessionManager)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenTopBar(navController: NavController, homeViewModel: HomeViewModel, selectedTab: MutableState<BottomNavItem>) {
    val menuExpanded = remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        title = {
            Text(selectedTab.value.label,
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
fun HomeContent(navController: NavHostController, sessionManager: SessionManager) {
    var userInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(id = R.drawable.bg_regular), // Your drawable
                contentScale = ContentScale.Crop // Crop to fit screen
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome to RecipeAI, Your space to create your own recipes.",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TextField(
            value = userInput,
            onValueChange = { userInput = it },
            label = { Text("What do you feel like eating?") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Button(
            onClick = {
                sessionManager.savePreference("customPreference", userInput)
                navController.navigate("recipeActivity") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Generate")
        }
    }
}


