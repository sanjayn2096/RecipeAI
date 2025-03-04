package com.sunj.recipeai

import com.sunj.recipeai.model.Recipes
import com.sunj.recipeai.model.UserData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

data class LoginRequest(val email: String, val token_id: String)
data class LoginResponse(val message: String?, val userId: String?)
data class SessionCheckRequest(val sessionId: String)
data class SessionCheckResponse(val message: String?, val userId: String?)
data class SignupRequest(val email: String, val password: String, val firstName: String, val lastName: String)
data class SignupResponse(val message: String?, val userId: String?)
data class SaveFavoriteRecipesRequest(val recipes: Recipes, val userId: String)
data class SaveFavoriteRecipesResponse(val message: String?)
data class SignoutRequest(val email: String)
data class SignoutResponse(val message: String)

interface ApiService {
    @POST("login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("signout")
    fun signout(@Body request: SignoutRequest): Call<SignoutResponse>

    @GET("fetch-user-details")
    fun fetchUserData(@Query("email") email: String?): Call<UserData>

    @POST("signup")
    @Headers("Content-Type: application/json")
    fun signup(@Body request: SignupRequest): Call<SignupResponse>

    @POST("check-session")
    fun checkSession(@Body request: SessionCheckRequest): Call<SessionCheckResponse>

    @POST("save-favorites")
    fun saveFavoriteRecipes(@Body request: SaveFavoriteRecipesRequest): Call<SaveFavoriteRecipesResponse>
}
