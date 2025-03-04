package com.sunj.recipeai.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sunj.recipeai.R
import com.sunj.recipeai.SessionManager
import com.sunj.recipeai.databinding.RecipeListItemBinding
import com.sunj.recipeai.model.Recipes
import com.sunj.recipeai.model.UserData
import com.sunj.recipeai.viewmodel.RecipeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class RecipeAdapter(private val listener: OnRecipeClickListener,
                    private val recipeViewModel: RecipeViewModel,
                    private val sessionManager: SessionManager,
                    private val lifecycleScope: LifecycleCoroutineScope,
                    private val userData: UserData
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    private var recipeList : List<Recipes> = listOf()
    private var favoriteJob: Job? = null

    interface OnRecipeClickListener {
        fun onRecipeClick(recipe: Recipes)
    }

    inner class RecipeViewHolder(val binding: RecipeListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                listener.onRecipeClick(recipeList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = RecipeListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }

    fun checkRecipeFavorited(recipeName: String): Boolean {
        // flat map of recipe names
        val favoriteRecipesNames = userData.favorite_recipes!!.map { it.recipeName }
        return favoriteRecipesNames.contains(recipeName)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe: Recipes = recipeList[position]
        //Flawed logic, need to get favorite status from server
        val isFavorited = checkRecipeFavorited(recipe.recipeName)
        holder.binding.recipeName.text = recipe.recipeName

        Glide.with(holder.binding.recipeImage.context)
            .load(recipe.image)
            .apply(
                RequestOptions()
                .placeholder(R.drawable.food_pic) // Placeholder image
            )
            .into(holder.binding.recipeImage)

        holder.binding.cuisineName.text = "Cuisine : " + recipe.cuisine
        holder.binding.cookingTime.text = "Time to Cook : " + recipe.cookingTime
        holder.binding.favButton.setImageResource(
            if (isFavorited) R.drawable.favorite_icon else R.drawable.unliked_favorite_icon
        )
        holder.binding.favButton.setOnClickListener {
            val newFavStatus = !recipe.isFavorite
            recipeList[position].isFavorite = newFavStatus

            // Update UI immediately
            holder.binding.favButton.setImageResource(
                if (newFavStatus) R.drawable.favorite_icon else R.drawable.unliked_favorite_icon
            )

            // Cancel any existing API calls
            favoriteJob?.cancel()

            // Delay API call by 500ms to debounce
            favoriteJob = lifecycleScope.launch {
                delay(2000)  // Wait for 2000ms before making API call
                if(!isFavorited) {
                    recipeViewModel.modifyFavoriteRecipe(
                        sessionManager.getUserId()!!,
                        recipeList[position]
                    )
                }
            }
        }
    }


    override fun getItemCount(): Int = recipeList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(feed: List<Recipes>) {
        recipeList = feed
        notifyDataSetChanged()
    }
}


