package com.sunj.recipeai.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.sunj.recipeai.R
import com.sunj.recipeai.Recipes

class RecipeFragment : Fragment(R.layout.fragment_recipe) {
    companion object {
        private const val ARG_RECIPE = "arg_recipe"

        fun newInstance(recipe: Recipes): RecipeFragment {
            val fragment = RecipeFragment()
            val bundle = Bundle().apply {
                putParcelable(ARG_RECIPE, recipe)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var recipe: Recipes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            recipe = it.getParcelable(ARG_RECIPE)!!
        } ?: run {
            // Log an error or handle the missing recipe
            Log.e("RecipeFragment", "Recipe data is missing!")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recipe, container, false)

        val recipeImage: ImageView = view.findViewById(R.id.recipe_image)
        val recipeInstructions: TextView = view.findViewById(R.id.recipe_instructions)
        val recipeName: TextView = view.findViewById(R.id.recipe_name)
        val ingredients: TextView = view.findViewById(R.id.recipe_ingredients)
        val intoButton: ImageView = view.findViewById(R.id.into_button)

        recipeName.text = recipe.recipeName
        recipeImage.setImageResource(R.drawable.food_pic)
        recipeInstructions.text = recipe.instructions
        ingredients.text = recipe.ingredients


        intoButton.setOnClickListener {
            requireActivity().findViewById<View>(R.id.fragment_container).visibility = View.GONE
            requireActivity().findViewById<View>(R.id.recipe_recycler_view).visibility = View.VISIBLE
            requireActivity().findViewById<View>(R.id.edit_preferences_button).visibility = View.VISIBLE
            requireActivity().findViewById<View>(R.id.refresh_recipes_button).visibility = View.VISIBLE
        }
        return view
    }
}
