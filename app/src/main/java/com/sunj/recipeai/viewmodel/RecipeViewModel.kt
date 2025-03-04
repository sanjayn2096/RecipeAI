package com.sunj.recipeai.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sunj.recipeai.model.Recipes
import kotlinx.coroutines.launch

import android.util.Log
import androidx.lifecycle.*
import com.sunj.recipeai.RetrofitClient
import com.sunj.recipeai.SaveFavoriteRecipesRequest
import com.sunj.recipeai.SaveFavoriteRecipesResponse
import com.sunj.recipeai.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecipeViewModel(private val key: String, private val sessionManager: SessionManager) : ViewModel() {

    private val _recipe = MutableLiveData<List<Recipes>>()
    val recipe: LiveData<List<Recipes>> get() = _recipe

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isBeingEdited = MutableLiveData<Boolean>()
    val isBeingEdited: LiveData<Boolean> get() = _isBeingEdited

    private lateinit var repository: RecipeRepository

    fun isBeingEdited() {
        _isBeingEdited.postValue(true)
    }

    fun callFetchRecipes() {
        if (key.isEmpty()) {
            throw Exception("Key is null, error in fetching the recipes")
        } else {
            repository = RecipeRepository(key)
            _isLoading.value = true
            viewModelScope.launch {
                val prompt = constructPrompt()
                val resp = repository.fetchRecipes(prompt)
                println(resp)
                _recipe.postValue(resp)
                _isLoading.value = false
            }
        }
    }

    private fun constructPrompt(): String {
        val selectedDietaryRestriction = sessionManager.getDietRestrictions()
        val selectedCuisine = sessionManager.getCuisine()
        val selectedMood = sessionManager.getMood()
        val selectedCookingPreference = sessionManager.getCookingPreference()

        return if (selectedMood == "lucky") {
            Log.d("RecipeViewModel", "selectedMood is I am feeling lucky")
            "You are my recipe book. I'm feeling Lucky today, please suggest me any recipe "+
            "Output the recipes in this format. " +
                    "Recipe = {'recipeName': string, 'imageUrl': String, 'ingredients': String, 'instructions' : String, 'cookingTime' : String, 'cuisine' : String} " +
                    "Return Array<Recipe>. The ingredients and instructions should be in bullet points. Mention the ingredients which are optional or replacements. " +
                    "Find a suitable image for this recipe and give me a public URL for it."
        } else {
                    "You are my recipe book. Suggest some recipes for me based on the following preferences." +
                    "I am feeling : + $selectedMood + " + ", I have the following diet restrictions : :  + $selectedDietaryRestriction " +
                    ", I prefer spending + $selectedCookingPreference + time on cooking " +
                    "and I feel like eating this cuisine :  + $selectedCuisine" +
                    "Output the recipes in this format. " +
                    "Recipe = {'recipeId': uuid, 'recipeName': string, 'imageUrl': String, 'ingredients': String, 'instructions' : String, 'cookingTime' : String, 'cuisine' : String} " +
                    "Return Array<Recipe>. The ingredients and instructions should be in bullet points. " +
                    "Mention the ingredients which are optional or replacements. " +
                    "Find a suitable image for this recipe and give me a public URL for it."
        }
    }

    fun modifyFavoriteRecipe(userId: String, recipe: Recipes) {
        RetrofitClient.instance.saveFavoriteRecipes(SaveFavoriteRecipesRequest(recipe, userId))
            .enqueue(object : Callback<SaveFavoriteRecipesResponse> {
                override fun onResponse(call: Call<SaveFavoriteRecipesResponse>, response: Response<SaveFavoriteRecipesResponse>) {
                    Log.d("RecipeViewModel", "favoriteRecipe, response success code = " + response.isSuccessful)
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        Log.d("RecipeViewModel", "favoriteRecipe, response body = $responseBody")
                    } else {
                        // Handle error responses manually
                        Log.e("RecipeViewModel", "favoriteRecipe, Error response: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<SaveFavoriteRecipesResponse>, t: Throwable) {
                    Log.e("RecipeViewModel", "favoriteRecipe, Network error: ${t.message}")
                }
            })
    }

}
