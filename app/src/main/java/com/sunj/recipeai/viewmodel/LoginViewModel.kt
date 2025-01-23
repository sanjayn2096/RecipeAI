package com.sunj.recipeai.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.sunj.recipeai.database.User

import com.sunj.recipeai.database.UserDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class LoginViewModel(userDb: UserDatabase) : ViewModel() {

    private val _isLoggedIn = MutableLiveData<Boolean>(false)
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    private val _userStatus = MutableLiveData<Boolean>(true)
    val userStatus: LiveData<Boolean> = _userStatus

    private val userDao = userDb.userDao()

    /**
     * Attempts to log in with the given username and password.
     * If successful, sets isLoggedIn to true.
     * If no such user exists, sets userStatus to false (triggering the sign-up UI).
     * If the user is found but password doesn't match, sets an errorMessage.
     */
    suspend fun login(username: String, password: String) {
        withContext(Dispatchers.IO) {
            val user = userDao.getUser(username, password)
            if (user != null) {
                _isLoggedIn.postValue(true)
            } else {
                // Check if user exists with a different password (wrong credentials)
                val existingUser = userDao.getUserByUsername(username)
                if (existingUser != null) {
                    // User exists but password is incorrect
                    _errorMessage.postValue("Invalid credentials. Please try again.")
                } else {
                    // User does not exist at all
                    _userStatus.postValue(false)
                }
            }
        }
    }

    /**
     * Attempts to sign up a new user with the given details.
     * If successful, sets isLoggedIn to true.
     * If not, sets an error message.
     */
    suspend fun signup(firstName: String, lastName: String, email: String, password: String) {
        withContext(Dispatchers.IO) {
            // Validate input
            if (email.isBlank() || !email.contains("@")) {
                _errorMessage.postValue("Please enter a valid email address.")
                return@withContext
            }

            if (password.isBlank()) {
                _errorMessage.postValue("Password cannot be empty.")
                return@withContext
            }

            val username = email  // Use email as username for simplicity
            // Check if user already exists
            val existingUser = userDao.getUserByUsername(username)
            if (existingUser != null) {
                _errorMessage.postValue("User already exists. Please log in instead.")
                return@withContext
            }

            // Create new user
            val newUser = User(
                userId = UUID.randomUUID().toString(),
                email = email,
                username = username,
                password = password,
                firstName = firstName,
                lastName = lastName
            )
            userDao.insertUser(newUser)
            _isLoggedIn.postValue(true)
        }
    }

    /**
     * Clears the current error message.
     */
    fun clearError() {
        _errorMessage.value = null
    }
}
