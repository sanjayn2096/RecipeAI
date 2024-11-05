package com.sunj.recipeai.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sunj.recipeai.model.Recipes
import kotlinx.coroutines.launch

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.*

class RecipeViewModel(private val key: String, context: Context) : ViewModel() {

    private val _recipe = MutableLiveData<List<Recipes>>()
    val recipe: LiveData<List<Recipes>> get() = _recipe

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val sharedPref: SharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)

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
                _recipe.postValue(resp)
                _isLoading.value = false
            }
        }
    }

    private fun constructPrompt(): String {
        val selectedDietaryRestriction = sharedPref.getString("selected_dietary_restriction", "No Restrictions Apply")
        val selectedCuisine = sharedPref.getString("selected_cuisine_preference", "No Cuisine Selected")
        val selectedMood = sharedPref.getString("selected_mood", "")
        val selectedCookingPreference = sharedPref.getString("selected_cooking_preference", "")

        return "You are my recipe book. Give me some recipes based on the following preferences I am " +
                "feeling : + $selectedMood + " + " My dietary restriction is : " + selectedDietaryRestriction +
                ", My cooking preference is : " + selectedCookingPreference +
                "and I feel like eating this cuisine : " + selectedCuisine +
                " Output the recipes in this format. " +
                "Recipe = {'recipeName': string, 'imageUrl': String, 'ingredients': String, 'instructions' : String, 'nutritionalValue' : String, 'Description of Dish' : String} " +
                "Return Array<Recipe>. The ingredients and instructions should be in bullet points"
    }
}
