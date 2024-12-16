package com.sunj.recipeai.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sunj.recipeai.R
import com.sunj.recipeai.databinding.RecipeListItemBinding
import com.sunj.recipeai.model.Recipes


class RecipeAdapter(private val listener: OnRecipeClickListener) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    private var recipeList : List<Recipes> = listOf()
    private var _onClickRecipe = MutableLiveData<Boolean>()
    val onClickRecipe: LiveData<Boolean> = _onClickRecipe

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
        val recipe: Recipes = recipeList[position]
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
    }

    override fun getItemCount(): Int = recipeList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(feed: List<Recipes>) {
        recipeList = feed
        notifyDataSetChanged()
    }
}


