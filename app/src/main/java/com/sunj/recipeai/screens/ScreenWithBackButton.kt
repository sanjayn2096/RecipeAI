package com.sunj.recipeai.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sunj.recipeai.ui.BackButton

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