package com.sunj.recipeai.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.sunj.recipeai.R
import com.sunj.recipeai.activities.RecipeActivity

class CookingPreferencesFragment : Fragment(R.layout.fragment_cooking_preference) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get SharedPreferences instance
        val sharedPref: SharedPreferences =
            requireContext().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)

        // Find the button by its ID
        val nextButton: Button = view.findViewById(R.id.Button_next_cooking_preferences)
        val backButton: ImageButton = view.findViewById(R.id.back_button_cooking_preferences)

        // Set a click listener for the button
        nextButton.setOnClickListener {
            // Check which RadioButton is selected
            val cookingPreference: String = when {
                view.findViewById<RadioButton>(R.id.radioButton_cooking).isChecked -> getString(R.string._10_minutes)
                view.findViewById<RadioButton>(R.id.radioButton_cooking_2).isChecked -> getString(R.string._10_30_minutes)
                view.findViewById<RadioButton>(R.id.radioButton_cooking_3).isChecked -> getString(R.string._30_60_minutes)
                view.findViewById<RadioButton>(R.id.radioButton_cooking_4).isChecked -> getString(R.string._60_minutes)
                else -> "Any Amount of time"
            }

            // Write to SharedPreferences
            with(sharedPref.edit()) {
                putString("selected_cooking_preference", cookingPreference)
                commit()
            }
            // Optional: Show a confirmation toast
            Toast.makeText(requireContext(), "Cooking Preference saved: $cookingPreference", Toast.LENGTH_SHORT).show()

            this.startActivity(Intent(requireContext(), RecipeActivity::class.java))
        }

        backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStackImmediate()
        }
    }
}
