package com.sunj.recipeai.viewmodel

import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sunj.recipeai.model.Recipes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipeRepository (private val key : String) {

    suspend fun fetchRecipes(prompt: String): List<Recipes> {
        return withContext(Dispatchers.IO) {
            try {
                val generativeModel = GenerativeModel(
                    modelName = "gemini-1.5-flash",
                    apiKey = key,
                    generationConfig = generationConfig {
                        responseMimeType = "application/json"
                    }
                )
                // Assuming generateContent returns a JSON string
                val jsonResponse = generativeModel.generateContent(prompt)

                Log.d("RecipeRepository", "JSON Response(jsonResponse) = " + jsonResponse.text)
                // Parse the JSON response into a List<Recipes> using Gson
                val gson = Gson()
                val listType = object : TypeToken<List<Recipes>>() {}.type
                gson.fromJson(jsonResponse.text, listType) as List<Recipes>

            } catch (e: Exception) {
                e.printStackTrace() // Handle the exception
                Log.d("RecipeActivity", "Error fetching recipes: ${e.message}")
                listOf()
            }
        }
    }
}