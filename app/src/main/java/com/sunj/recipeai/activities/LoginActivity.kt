package com.sunj.recipeai.activities

import android.content.Intent
import android.os.Bundle
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
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.sunj.recipeai.viewmodel.IngredientsViewModel
import com.sunj.recipeai.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
           LoginScreen()
        }
    }

    @Composable
    fun LoginScreen() {
        val userStatus by loginViewModel.userStatus.observeAsState(true)
        val loginState by loginViewModel.isLoading.observeAsState(false)
        var userName by rememberSaveable { mutableStateOf("") }
        var passWord by rememberSaveable { mutableStateOf("") }
        loginViewModel = LoginViewModel(this.application)
        LaunchedEffect(loginState) {
            if (loginState) {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            }
        }

        if(!userStatus) {
            SignupScreen()
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = CenterHorizontally
            ) {
                TextField(
                    value = userName,
                    onValueChange = { userName = it },
                    label = { Text("Username/Email") }
                )

                TextField(
                    value = passWord,
                    onValueChange = { passWord = it },
                    label = { Text("Password") }
                )

                Button(
                    onClick = {
                        lifecycleScope.launch {
                            loginViewModel.login(userName, passWord)
                        }
                    },
                ) {
                    Text("Login")
                }
            }
        }
    }


    @Composable
    fun SignupScreen() {
        var firstName by rememberSaveable { mutableStateOf("") }
        var lastName by rememberSaveable { mutableStateOf("") }
        var emailId by rememberSaveable { mutableStateOf("") }
        var password by rememberSaveable { mutableStateOf(value = "") }
        var showPassword by rememberSaveable { mutableStateOf(value = false) }
        var renterPassword by rememberSaveable { mutableStateOf(value = "") }


        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = CenterHorizontally
        ) {
            TextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name") }
            )

            Spacer(Modifier.height(10.dp))

            TextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") }
            )

            Spacer(Modifier.height(10.dp))

            TextField(
                value = emailId,
                onValueChange = { emailId = it },
                label = { Text("Email ID") }
            )

            Spacer(Modifier.height(10.dp))


            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = password,
                onValueChange = { newText ->
                    password = newText
                },
                label = {
                    Text(text = "Password")
                },
                placeholder = { Text(text = "Type password here") },
                shape = RoundedCornerShape(percent = 20),
                visualTransformation = if (showPassword) {
                    VisualTransformation.None

                } else {
                    PasswordVisualTransformation()

                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    if (showPassword) {
                        IconButton(onClick = { showPassword = false }) {
                            Icon(
                                imageVector = Icons.Filled.Visibility,
                                contentDescription = "hide_password"
                            )
                        }
                    } else {
                        IconButton(
                            onClick = { showPassword = true }) {
                            Icon(
                                imageVector = Icons.Filled.VisibilityOff,
                                contentDescription = "hide_password"
                            )
                        }
                    }
                }
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = password,
                onValueChange = { newText ->
                    password = newText
                },
                label = {
                    Text(text = "Password")
                },
                placeholder = { Text(text = "Type password here") },
                shape = RoundedCornerShape(percent = 20),
                visualTransformation = if (showPassword) {
                    VisualTransformation.None

                } else {
                    PasswordVisualTransformation()

                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    if (showPassword) {
                        IconButton(onClick = { showPassword = false }) {
                            Icon(
                                imageVector = Icons.Filled.Visibility,
                                contentDescription = "hide_password"
                            )
                        }
                    } else {
                        IconButton(
                            onClick = { showPassword = true }) {
                            Icon(
                                imageVector = Icons.Filled.VisibilityOff,
                                contentDescription = "hide_password"
                            )
                        }
                    }
                }
            )

            Spacer(Modifier.height(20.dp))


            Button(
                onClick = {
                    lifecycleScope.launch {
                        loginViewModel.login(firstName + lastName, password)
                    }
                },
            ) {
                Text("Login")
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewLoginScreen() {
        LoginScreen()
    }
}
