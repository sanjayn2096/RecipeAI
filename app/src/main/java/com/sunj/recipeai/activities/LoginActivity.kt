package com.sunj.recipeai.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.sunj.recipeai.R
import com.sunj.recipeai.viewmodel.LoginViewModel
import com.sunj.recipeai.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        supportActionBar?.hide()
        loginViewModel = ViewModelProvider(
            this, ViewModelFactory(applicationContext)
        )[LoginViewModel::class.java]

        //setContent { MainScreen() }

        // Check if user is already logged in
        lifecycleScope.launch {
            loginViewModel.checkSession() // Start session validation
        }

        loginViewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                setContent { MainScreen() }  // Show loading screen
            } else {
                loginViewModel.isLoggedIn.observe(this) { isLoggedIn ->
                    if (isLoggedIn) {
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                        finish()
                    } else {
                        setContent { MainLoginView(loginViewModel) }
                    }
                }
            }
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

        // Navigate to HomeActivity on success
        LaunchedEffect(isLoggedIn) {
            if (isLoggedIn) {
                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                startActivity(intent)
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isShowingSignUp) {
                SignupScreen(
                    onSignUpClicked = { firstName, lastName, email, password ->
                        lifecycleScope.launch {
                            loginViewModel.signup(email, password, firstName, lastName)
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
    fun MainScreen() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .paint(painterResource(id = R.drawable.img), contentScale = ContentScale.Crop),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.recipeai),
                    fontSize = 36.sp,
                    fontFamily = FontFamily(Font(R.font.titan_one)),
                    color = colorResource(id = R.color.custom_green),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp),
                    style = TextStyle(
                        shadow = Shadow(
                            color = colorResource(id = R.color.light_gray),
                            offset = Offset(0f, 7f),
                            blurRadius = 1f
                        )
                    )
                )

                Text(
                    text = stringResource(id = R.string.let_s_cook_something_nice_today),
                    fontSize = 15.sp,
                    fontFamily = FontFamily(Font(R.font.custom_font_source_sans)),
                    color = colorResource(id = R.color.black),
                    textAlign = TextAlign.Center
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
        var emailError by rememberSaveable { mutableStateOf<String?>(null) }
        var passwordError by rememberSaveable { mutableStateOf<String?>(null) }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.8f),
                maxLines = 1,
                value = userName,
                onValueChange = {
                    userName = it
                    emailError = null  // Clear error when user types
                },
                label = { Text("Username/Email") },
                shape = RoundedCornerShape(percent = 20),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(ContextCompat.getColor(this@LoginActivity, R.color.custom_green))
                ),
                isError = emailError != null,
                supportingText = { emailError?.let { Text(it, color = Color.Red) } }
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.8f),
                maxLines = 1,
                value = password,
                onValueChange = {
                    password = it
                    passwordError = null  // Clear error when user types
                },
                label = { Text("Password") },
                shape = RoundedCornerShape(percent = 20),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(ContextCompat.getColor(this@LoginActivity, R.color.custom_green))
                ),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = passwordError != null,
                supportingText = { passwordError?.let { Text(it, color = Color.Red) } }
            )

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    val trimmedEmail = userName.trim()
                    val trimmedPassword = password.trim()

                    if (trimmedEmail.isEmpty()) {
                        emailError = "Email cannot be empty"
                        return@Button
                    }
                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches()) {
                        emailError = "Invalid email format"
                        return@Button
                    }
                    if (trimmedPassword.isEmpty()) {
                        passwordError = "Password cannot be empty"
                        return@Button
                    }

                    onLoginClicked(trimmedEmail, trimmedPassword)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(ContextCompat.getColor(this@LoginActivity, R.color.custom_green))
                )
            ) {
                Text("Login")
            }

            Spacer(Modifier.height(10.dp))

            Button(
                onClick = { onSignUpClicked() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(ContextCompat.getColor(this@LoginActivity, R.color.custom_green))
                )
            ) {
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.8f),
                value = firstName,
                maxLines = 1,
                onValueChange = { firstName = it },
                label = { Text("First Name") },
                shape = RoundedCornerShape(percent = 20)
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.8f),
                value = lastName,
                maxLines = 1,
                onValueChange = { lastName = it },
                label = { Text("Last Name") },
                shape = RoundedCornerShape(percent = 20)
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.8f),
                value = emailId,
                maxLines = 1,
                onValueChange = { emailId = it },
                label = { Text("Email ID") },
                shape = RoundedCornerShape(percent = 20)
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.8f),
                value = password,
                maxLines = 1,
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
                maxLines = 1,
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
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(ContextCompat.getColor(this@LoginActivity, R.color.custom_green)))
            ) {
                Text("Sign Up")
            }

            Spacer(Modifier.height(10.dp))

            Button(
                onClick = { onBackToLoginClicked() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(ContextCompat.getColor(this@LoginActivity, R.color.custom_green)))
            ) {
                Text("Back to Login")
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewLoginScreen() {
        MainScreen()
    }
}
