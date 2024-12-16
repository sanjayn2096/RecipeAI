package com.sunj.recipeai.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.sunj.recipeai.R
import com.sunj.recipeai.adapters.RecipeAdapter
import com.sunj.recipeai.model.Recipes
import com.sunj.recipeai.databinding.ActivityDisplayRecipeBinding
import com.sunj.recipeai.fragments.RecipeFragment
import com.sunj.recipeai.viewmodel.RecipeViewModel
import com.sunj.recipeai.viewmodel.ViewModelFactory

class RecipeActivity : AppCompatActivity(), RecipeAdapter.OnRecipeClickListener {
    private lateinit var loadingAnimation: LottieAnimationView
    private lateinit var recyclerView: RecyclerView
    private lateinit var textViewLoading: TextView
    private lateinit var editButton: ImageButton
    private lateinit var refreshButton: ImageButton
    private lateinit var recipeViewModel: RecipeViewModel

    // Declare the View Binding object
    private lateinit var binding: ActivityDisplayRecipeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize View Binding
        binding = ActivityDisplayRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // Hide the action bar if available.
        supportActionBar?.hide()

        recyclerView = binding.recipeRecyclerView
        loadingAnimation = binding.loadingAnimation
        textViewLoading = binding.textViewLoading
        refreshButton = binding.refreshRecipesButton
        editButton = binding.editPreferencesButton

        recipeViewModel = ViewModelProvider(this, ViewModelFactory(applicationContext))[RecipeViewModel::class.java]

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recipeRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val recipeAdapter = RecipeAdapter(this@RecipeActivity)

        // Observe the recipe list to update RecyclerView data
        recipeViewModel.recipe.observe(this, Observer { feed ->
            recipeAdapter.setData(feed)
        })

        // Observe the loading state to show/hide loading animation
        recipeViewModel.isLoading.observe(this, Observer { isLoading ->
            showLoading(isLoading)
        })

        // Trigger API call
        recipeViewModel.callFetchRecipes()
        recyclerView.adapter = recipeAdapter
        recyclerView.visibility = View.VISIBLE

        // Set click listeners using the binding object
        editButton.setOnClickListener {
            this.finish()
        }

        refreshButton.setOnClickListener {
            recipeViewModel.callFetchRecipes()
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
        val fragmentContainer = binding.fragmentContainer
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
