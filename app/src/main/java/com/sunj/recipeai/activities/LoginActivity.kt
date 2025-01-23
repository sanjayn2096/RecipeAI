package com.sunj.recipeai.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.sunj.recipeai.viewmodel.LoginViewModel
import com.sunj.recipeai.viewmodel.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        loginViewModel = ViewModelProvider(
            this, ViewModelFactory(applicationContext)
        )[LoginViewModel::class.java]
        setContent {
            MainLoginView(loginViewModel)
        }
    }

    @Composable
    fun MainLoginView(loginViewModel: LoginViewModel) {
        val userStatus by loginViewModel.userStatus.observeAsState(true)
        val isLoggedIn by loginViewModel.isLoggedIn.observeAsState(false)
        val errorMessage by loginViewModel.errorMessage.observeAsState()

        var isShowingSignUp by rememberSaveable { mutableStateOf(false) }

        // Switch to signup if user not found
        LaunchedEffect(userStatus) {
            if (!userStatus) {
                isShowingSignUp = true
            }
        }

        // Navigate to MainActivity on success
        LaunchedEffect(isLoggedIn) {
            if (isLoggedIn) {
                lifecycleScope.launch {
                    withContext(Dispatchers.Main) {
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    }
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = CenterHorizontally
        ) {
            if (isShowingSignUp) {
                SignupScreen(
                    onSignUpClicked = { firstName, lastName, email, password ->
                        lifecycleScope.launch {
                            loginViewModel.signup(firstName, lastName, email, password)
                        }
                    },
                    onBackToLoginClicked = {
                        isShowingSignUp = false
                    }
                )
            } else {
                LoginScreen(
                    onLoginClicked = { username, password ->
                        lifecycleScope.launch {
                            loginViewModel.login(username, password)
                        }
                    },
                    onSignUpClicked = {
                        isShowingSignUp = true
                    }
                )
            }

            // Error dialog
            if (errorMessage != null) {
                AlertDialog(
                    onDismissRequest = { loginViewModel.clearError() },
                    title = { Text("Error") },
                    text = { Text(errorMessage!!) },
                    confirmButton = {
                        Button(onClick = { loginViewModel.clearError() }) {
                            Text("OK")
                        }
                    }
                )
            }
        }
    }

    @Composable
    fun LoginScreen(
        onLoginClicked: (String, String) -> Unit,
        onSignUpClicked: () -> Unit
    ) {
        var userName by rememberSaveable { mutableStateOf("") }
        var password by rememberSaveable { mutableStateOf("") }

        Column(
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.8f),
                value = userName,
                onValueChange = { userName = it },
                label = { Text("Username/Email") },
                shape = RoundedCornerShape(percent = 20)
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.8f),
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                shape = RoundedCornerShape(percent = 20),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(Modifier.height(20.dp))

            Button(onClick = { onLoginClicked(userName, password) }) {
                Text("Login")
            }

            Spacer(Modifier.height(10.dp))

            Button(onClick = { onSignUpClicked() }) {
                Text("Sign Up")
            }
        }
    }

    @Composable
    fun SignupScreen(
        onSignUpClicked: (String, String, String, String) -> Unit,
        onBackToLoginClicked: () -> Unit
    ) {
        var firstName by rememberSaveable { mutableStateOf("") }
        var lastName by rememberSaveable { mutableStateOf("") }
        var emailId by rememberSaveable { mutableStateOf("") }
        var password by rememberSaveable { mutableStateOf("") }
        var showPassword by rememberSaveable { mutableStateOf(false) }
        var reEnterPassword by rememberSaveable { mutableStateOf("") }

        val passwordsMatch = password == reEnterPassword

        Column(
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.8f),
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name") },
                shape = RoundedCornerShape(percent = 20)
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.8f),
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") },
                shape = RoundedCornerShape(percent = 20)
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.8f),
                value = emailId,
                onValueChange = { emailId = it },
                label = { Text("Email ID") },
                shape = RoundedCornerShape(percent = 20)
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.8f),
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                placeholder = { Text("Type password here") },
                shape = RoundedCornerShape(percent = 20),
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val icon = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(imageVector = icon, contentDescription = "Toggle Password Visibility")
                    }
                }
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.8f),
                value = reEnterPassword,
                onValueChange = { reEnterPassword = it },
                label = { Text("Re-enter Password") },
                placeholder = { Text("Re-type password here") },
                shape = RoundedCornerShape(percent = 20),
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val icon = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(imageVector = icon, contentDescription = "Toggle Password Visibility")
                    }
                }
            )

            if (!passwordsMatch && password.isNotEmpty() && reEnterPassword.isNotEmpty()) {
                Spacer(Modifier.height(5.dp))
                Text(
                    "Passwords don't match",
                    color = androidx.compose.ui.graphics.Color.Red
                )
            }

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    if (passwordsMatch && password.isNotEmpty()) {
                        onSignUpClicked(firstName, lastName, emailId, password)
                    }
                }
            ) {
                Text("Sign Up")
            }

            Spacer(Modifier.height(10.dp))

            Button(onClick = { onBackToLoginClicked() }) {
                Text("Back to Login")
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewLoginScreen() {
        LoginScreen(
            onLoginClicked = { _, _ -> },
            onSignUpClicked = {}
        )
    }
}
