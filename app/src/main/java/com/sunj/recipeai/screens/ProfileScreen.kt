package com.sunj.recipeai.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sunj.recipeai.model.UserData

@Composable
fun ProfileScreen(navController: NavController, userData: UserData?) {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
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