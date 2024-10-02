package com.sunj.recipeai

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


class RecipeAdapter(private val recipeList: List<Recipes>, private val listener: OnRecipeClickListener) :
    RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    interface OnRecipeClickListener {
        fun onRecipeClick(recipe: Recipes)
    }

    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeImage: ImageView = itemView.findViewById(R.id.recipe_image)
        val recipeName: TextView = itemView.findViewById(R.id.recipe_name)
        val cuisineName: TextView = itemView.findViewById(R.id.cuisine_name)
        val cookingTime: TextView = itemView.findViewById(R.id.cooking_time)

        init {
            itemView.setOnClickListener {
                listener.onRecipeClick(recipeList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recipe_list_item, parent, false)
        return RecipeViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val sharedPref: SharedPreferences =
            holder.itemView.context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)

        val selectedCuisine = sharedPref.getString("selected_cuisine_preference", "No Cuisine Selected")
        val selectedCookingPreference = sharedPref.getString("selected_cooking_preference", "")

        val recipe: Recipes = recipeList[position]
        holder.recipeName.text = recipe.recipeName

        Glide.with(holder.recipeImage.context)
            .load(recipe.image)
            .apply(
                RequestOptions()
                .placeholder(R.drawable.food_pic) // Placeholder image
            )
            .into(holder.recipeImage)

        // Fetch from sharedPreferences
        holder.cuisineName.text = "Cuisine : $selectedCuisine"
        holder.cookingTime.text = "Time to Cook : $selectedCookingPreference"
    }

    override fun getItemCount(): Int = recipeList.size
}


