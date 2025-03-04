package com.sunj.recipeai.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sunj.recipeai.R

class DietaryRestrictionFragment : Fragment(R.layout.fragment_dietary_restriction) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get SharedPreferences instance
        val sharedPref: SharedPreferences =
            requireContext().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)

        // Find the button by its ID
        val nextButton: Button = view.findViewById(R.id.Button_next_dietary_restrictions)
        val backButton: ImageButton = view.findViewById(R.id.back_button_dietary_preferences)

        // Set a click listener for the button
        nextButton.setOnClickListener {
            // Check which RadioButton is selected
            val diet: String = when {
                view.findViewById<RadioButton>(R.id.radioButton_vegetarian).isChecked -> getString(
                    R.string.vegetarian
                )
                view.findViewById<RadioButton>(R.id.radioButton_vegan).isChecked -> getString(
                    R.string.vegan
                )
                view.findViewById<RadioButton>(R.id.radioButton_pescitarian).isChecked -> getString(
                    R.string.pescitarian
                )
                view.findViewById<RadioButton>(R.id.radioButton_non_vegetarian).isChecked -> getString(
                    R.string.non_vegetarian_without_red_meat
                )
                view.findViewById<RadioButton>(R.id.radioButton_gluten_free).isChecked -> getString(
                    R.string.gluten_free
                )
                view.findViewById<RadioButton>(R.id.radioButton_no_restrictions).isChecked -> getString(
                    R.string.no_restrictions
                )
                else -> "No Restrictions Apply"
            }

            // Write to SharedPreferences
            with(sharedPref.edit()) {
                putString("selected_dietary_restriction", diet)
                commit()
            }

            findNavController().navigate(R.id.action_dietRestrictionsFragment_to_cuisineFragment)
        }

        backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}
