package com.sunj.recipeai

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sunj.recipeai.databinding.RecipeListItemBinding

class RecipeAdapter(private val recipeList: List<Recipes>, private val listener: OnRecipeClickListener) :
    RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val sharedPref: SharedPreferences =
            holder.itemView.context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)

        val selectedCuisine = sharedPref.getString("selected_cuisine_preference", "No Cuisine Selected")
        val selectedCookingPreference = sharedPref.getString("selected_cooking_preference", "")

        val recipe: Recipes = recipeList[position]
        holder.binding.recipeName.text = recipe.recipeName

        Glide.with(holder.binding.recipeImage.context)
            .load(recipe.image)
            .apply(
                RequestOptions()
                .placeholder(R.drawable.food_pic) // Placeholder image
            )
            .into(holder.binding.recipeImage)

        // Fetch from sharedPreferences
        holder.binding.cuisineName.text = "Cuisine : $selectedCuisine"
        holder.binding.cookingTime.text = "Time to Cook : $selectedCookingPreference"
    }

    override fun getItemCount(): Int = recipeList.size
}


