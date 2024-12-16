package com.sunj.recipeai.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.sunj.recipeai.database.User

import com.sunj.recipeai.database.UserDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginViewModel(application: Application) : AndroidViewModel(application){
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _userStatus = MutableLiveData<Boolean>()
    val userStatus: LiveData<Boolean> get() = _userStatus

    private val userDb = Room.databaseBuilder(
        application,
        UserDatabase::class.java, "user-database"
    ).build()

    private val userDao = userDb.userDao()

    suspend fun login(username: String, password: String) {
        _isLoading.value = false
        // Perform login logic here
        withContext(Dispatchers.IO) {
            val user = userDao.getUser(username, password)
            if (user != null) {
                // Login successful
                _isLoading.value = true
            } else {
               _userStatus.value = false
            }
        }
    }

}