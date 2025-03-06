package com.sunj.recipeai.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sunj.recipeai.network.RetrofitClient
import com.sunj.recipeai.SessionManager
import com.sunj.recipeai.network.SignoutRequest
import com.sunj.recipeai.network.SignoutResponse
import com.sunj.recipeai.model.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val sessionManager: SessionManager) : ViewModel() {

    private val _userLiveData = MutableLiveData<UserData?>()
    val userLiveData: LiveData<UserData?> = _userLiveData

    private val _isSignedOut = MutableLiveData<Boolean?>()
    val isSignedOut: LiveData<Boolean?> = _isSignedOut

    fun getUserDetails() {
        val email = sessionManager.getEmail()
        RetrofitClient.instance.fetchUserData(email)
            .enqueue(object : Callback<UserData> {
                override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                    Log.d("HomeViewModel", "response success code = " + response.isSuccessful)
                    if (response.isSuccessful) {
                        Log.d("HomeViewModel", "response body = " + response.body())
                        _userLiveData.postValue(response.body())
                    } else {
                        Log.e("HomeViewModel", "Error response: ${response.errorBody()?.string()}")
                        _userLiveData.postValue(null)
                    }
                }
                override fun onFailure(call: Call<UserData>, t: Throwable) {
                    Log.e("HomeViewModel", "Network error: ${t.message}")
                    _userLiveData.postValue(null)
                }
            })
    }

    fun signOut() {
        val email = sessionManager.getEmail()
        Log.d("HomeViewModel", "signOut, email = $email")
        RetrofitClient.instance.signout(SignoutRequest(email!!))
            .enqueue(object : Callback<SignoutResponse> {
                override fun onResponse(call: Call<SignoutResponse>, response: Response<SignoutResponse>) {
                    Log.d("HomeViewModel", "signOut, response success code = " + response.isSuccessful)
                    if (response.isSuccessful) {
                        _isSignedOut.postValue(true)
                        sessionManager.clearSession()
                    } else {
                        Log.e("HomeViewModel", "signOut, Error response: ${response.errorBody()?.string()}")
                        _isSignedOut.postValue(false)
                    }
                }
                override fun onFailure(call: Call<SignoutResponse>, t: Throwable) {
                    Log.e("HomeViewModel", "Network error: ${t.message}")
                    _isSignedOut.postValue(false)
                }
            })
    }
}