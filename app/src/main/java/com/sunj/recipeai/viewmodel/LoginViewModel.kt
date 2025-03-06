package com.sunj.recipeai.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.sunj.recipeai.network.LoginRequest
import com.sunj.recipeai.network.LoginResponse
import com.sunj.recipeai.network.RetrofitClient
import com.sunj.recipeai.network.SessionCheckRequest
import com.sunj.recipeai.network.SessionCheckResponse
import com.sunj.recipeai.SessionManager
import com.sunj.recipeai.network.SignupRequest
import com.sunj.recipeai.network.SignupResponse
import com.sunj.recipeai.database.UserDatabase
import com.sunj.recipeai.generateRandomId
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import org.json.JSONObject

class LoginViewModel(
    userDb : UserDatabase,
    private val sessionManager: SessionManager
) : ViewModel() {
    // Indicates whether user was found.
    private val _userStatus = MutableLiveData<Boolean>(true)
    val userStatus: LiveData<Boolean> get() = _userStatus

    private val _isLoading = MutableLiveData(true) // Initially true
    val isLoading: LiveData<Boolean> = _isLoading

    // Indicates whether the user is currently logged in.
    private val _isLoggedIn = MutableLiveData<Boolean>(false)
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    // Holds any error messages to be displayed in an alert dialog.
    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    fun checkSession() {
        val sessionId = sessionManager.getSession()
        _isLoading.value = true
        Log.d("LoginViewModel", "checkSession, Session ID: $sessionId")
        if (sessionId != null) {
            RetrofitClient.instance.checkSession(SessionCheckRequest(sessionId))
                .enqueue(object : Callback<SessionCheckResponse> {
                    override fun onResponse(
                        call: Call<SessionCheckResponse>,
                        response: Response<SessionCheckResponse>
                    ) {
                        val responseBody = response.body()
                        Log.d("LoginViewModel", "checkSession, response = ${response.code()}")
                        if (response.isSuccessful) {
                            Log.d("LoginViewModel", "checkSession, response body = $responseBody")
                            if(responseBody?.message != null) {
                                _isLoggedIn.value = true
                                _isLoading.value = false
                            }
                        } else {
                            Log.d("LoginViewModel", "checkSession, response body = $responseBody")
                            sessionManager.clearSession()
                            _isLoading.value = false
                            _isLoggedIn.value = false
                        }
                    }

                    override fun onFailure(call: Call<SessionCheckResponse>, t: Throwable) {
                        _isLoading.value = false
                        _isLoggedIn.value = false
                    }
                })
        } else {
            Log.d("LoginViewModel", "checkSession, No Session Saved")
            _isLoading.value = false
            _isLoggedIn.value = false
        }
    }

    fun login(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    user?.getIdToken(true)?.addOnSuccessListener { result ->
                        val firebaseToken = result.token
                        if (firebaseToken != null) {
                            val sessionId = generateRandomId() // Generate your own session ID
                            sessionManager.saveSession(sessionId)
                            Log.d("LoginViewModel", "Saving Session ID : " + sessionId)
                            sendSessionToBackend(email, sessionId)
                            _isLoggedIn.value = true
                        } else {
                            _errorMessage.value = "Failed to retrieve session"
                        }
                    }
                } else {
                    _errorMessage.value = task.exception?.localizedMessage ?: "Login failed"
                }
            }
            .addOnFailureListener {
                _errorMessage.value = "Network error: ${it.message}"
            }
    }


    fun sendSessionToBackend(email: String, sessionId: String) {
        RetrofitClient.instance.login(LoginRequest(email, sessionId))
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    Log.d("LoginViewModel", "save session response success code = " + response.isSuccessful)
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        Log.d("LoginViewModel", "Response body = $responseBody")
                        if (responseBody!=null) {
                            sessionManager.saveEmail(email)
                            sessionManager.saveUserId(responseBody.userId!!)
                        }
                    } else {
                        // Handle error responses manually
                        val errorBody = response.errorBody()?.string()
                        Log.e("LoginViewModel", "save session Error response: $errorBody")
                        _errorMessage.value = extractErrorMessage(errorBody)
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    _errorMessage.value = "save session Network error"
                }
            })
    }



    fun signup(email: String, password: String, firstName: String, lastName : String) {
        RetrofitClient.instance.signup(SignupRequest(email, password, firstName, lastName))
            .enqueue(object : Callback<SignupResponse> {
                override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                    Log.d("LoginViewModel", "response success code = " + response.isSuccessful)
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        Log.d("LoginViewModel", "response body = $responseBody")
                        if (responseBody?.userId != null) {
                            val sessionId = generateRandomId() // Generate your own session ID
                            sessionManager.saveSession(sessionId)
                            sessionManager.saveEmail(email)
                            sessionManager.saveUserId(responseBody.userId)
                            _isLoggedIn.value = true
                        } else {
                            _errorMessage.value = responseBody?.message ?: "Signup failed"
                        }
                    } else {
                        // Handle error responses manually
                        val errorBody = response.errorBody()?.string()
                        Log.e("LoginViewModel", "Error response: $errorBody")
                        _errorMessage.value = extractErrorMessage(errorBody)
                    }
                }

                override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                    _errorMessage.value = "Network error"
                }
            })
    }

    /**
     * Extracts the "error" message from a JSON error response.
     */
    private fun extractErrorMessage(errorBody: String?): String {
        return try {
            val jsonObject = JSONObject(errorBody ?: "{}")
            jsonObject.optString("error", "Signup failed")
        } catch (e: Exception) {
            "Signup failed"
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
