package com.sunj.recipeai.activities

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sunj.recipeai.R
import com.sunj.recipeai.RecipeAdapter
import com.sunj.recipeai.Recipes
import com.sunj.recipeai.fragments.RecipeFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecipeActivity : AppCompatActivity(), RecipeAdapter.OnRecipeClickListener {
    private lateinit var loadingAnimation: LottieAnimationView
    private lateinit var recyclerView: RecyclerView
    private lateinit var textViewLoading: TextView
    private lateinit var editButton: ImageButton
    private lateinit var refreshButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_recipe)

        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        supportActionBar?.hide()

        recyclerView = findViewById(R.id.recipe_recycler_view)
        loadingAnimation = findViewById(R.id.loading_animation)
        textViewLoading = findViewById(R.id.textViewLoading)
        refreshButton = findViewById(R.id.refresh_recipes_button)
        editButton = findViewById(R.id.edit_preferences_button)

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val sharedPref: SharedPreferences =
            getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)

        val selectedDietaryRestriction = sharedPref.getString("selected_dietary_restriction", "No Restrictions Apply")
        val selectedCuisine = sharedPref.getString("selected_cuisine_preference", "No Cuisine Selected")
        val selectedMood = sharedPref.getString("selected_mood", "")
        val selectedCookingPreference = sharedPref.getString("selected_cooking_preference", "")

        val prompt = "You are my recipe book. Give me some recipes based on the following preferences I am " +
                "feeling : + $selectedMood + " + " My dietary restriction is : " + selectedDietaryRestriction + ", My cooking preference is : " + selectedCookingPreference +
                "and I feel like eating this cuisine : " + selectedCuisine + " Output the recipes in this format. " +
                "Recipe = {'recipeName': string, 'imageUrl': String, 'ingredients': String, 'instructions' : String, 'nutritionalValue' : String, 'Description of Dish' : String} " +
                "Return Array<Recipe>. The ingredients and instructions should be in bullet points"

        callFetchRecipes(prompt)

        editButton.setOnClickListener {
            this.finish()
        }

        refreshButton.setOnClickListener {
            callFetchRecipes("Fetch more number of recipes based on the my previous preferences")
        }

    }

    private fun callFetchRecipes(prompt: String){
        lifecycleScope.launch {
            showLoading(true)
            val recipes = fetchRecipes(prompt)
            showLoading(false)
            if (recipes != null) {
                val adapter = RecipeAdapter(recipes, this@RecipeActivity)
                recyclerView.adapter = adapter
                recyclerView.visibility = View.VISIBLE
            } else {
                //AlertDialog(application, "Error fetching recipes, hit the refresh button to re-fetch")
                Toast.makeText(applicationContext, "Error fetching recipes, hit the refresh button to re-fetch", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun fetchRecipes(prompt: String): List<Recipes>? {
        return withContext(Dispatchers.Main) {
            try {
                val ai = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)

                val apiKey = ai.metaData.getString("com.sunj.recipeai.API_KEY")

                val generativeModel = GenerativeModel(
                    modelName = "gemini-1.5-flash",
                    apiKey = apiKey!!,
                    generationConfig = generationConfig {
                        responseMimeType = "application/json"
                    }
                )
                // Assuming generateContent returns a JSON string
                val jsonResponse = generativeModel.generateContent(prompt)

                // Parse the JSON response into a List<Recipes> using Gson
                val gson = Gson()
                val listType = object : TypeToken<List<Recipes>>() {}.type
                gson.fromJson(jsonResponse.text, listType) as List<Recipes>

            } catch (e: Exception) {
                e.printStackTrace() // Handle the exception
                Log.d("RecipeActivity", "Error fetching recipes: ${e.message}")
                null
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        loadingAnimation.visibility = if (isLoading) View.VISIBLE else View.GONE
        textViewLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
        recyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
        editButton.visibility = if (isLoading) View.GONE else View.VISIBLE
        refreshButton.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    override fun onRecipeClick(recipe: Recipes) {
        val fragment = RecipeFragment.newInstance(recipe)
        val fragmentContainer = findViewById<View>(R.id.fragment_container)
        fragmentContainer.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        editButton.visibility = View.GONE
        refreshButton.visibility = View.GONE

        // Open RecipeFragment and pass the recipe data
        Log.d("RecipeActivity", "RECIPE CLICKED")
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

}
