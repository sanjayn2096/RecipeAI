package com.sunj.recipeai.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sunj.recipeai.model.Recipes
import kotlinx.coroutines.launch

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.*

class RecipeViewModel(private val key: String, private val mSharedPreferences: SharedPreferences) : ViewModel() {

    private val _recipe = MutableLiveData<List<Recipes>>()
    val recipe: LiveData<List<Recipes>> get() = _recipe

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private lateinit var repository: RecipeRepository

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
        val selectedDietaryRestriction = mSharedPreferences.getString("selected_dietary_restriction", "No Restrictions Apply")
        val selectedCuisine = mSharedPreferences.getString("selected_cuisine_preference", "No Cuisine Selected")
        val selectedMood = mSharedPreferences.getString("selected_mood", "")
        val selectedCookingPreference = mSharedPreferences.getString("selected_cooking_preference", "")

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
                    "Recipe = {'recipeName': string, 'imageUrl': String, 'ingredients': String, 'instructions' : String, 'cookingTime' : String, 'cuisine' : String} " +
                    "Return Array<Recipe>. The ingredients and instructions should be in bullet points. " +
                    "Mention the ingredients which are optional or replacements. " +
                    "Find a suitable image for this recipe and give me a public URL for it."
        }
    }
}
