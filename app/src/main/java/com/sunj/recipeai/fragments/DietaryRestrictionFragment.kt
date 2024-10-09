package com.sunj.recipeai.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.Toast
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

        val mood: String = ""
        // Set a click listener for the button
        nextButton.setOnClickListener {
            // Check which RadioButton is selected
            if (view.findViewById<CheckBox>(R.id.radioButton_vegetarian).isChecked) {
                mood + getString(R.string.vegetarian)
            }
            if (view.findViewById<CheckBox>(R.id.radioButton_vegan).isChecked) {
                mood + getString(R.string.vegan)
            }
            if (view.findViewById<CheckBox>(R.id.radioButton_pescitarian).isChecked) {
                mood + getString(R.string.pescitarian)
            }
            if (view.findViewById<CheckBox>(R.id.radioButton_non_vegetarian).isChecked) {
                mood + getString(R.string.non_vegetarian_without_red_meat)
            }
            if (view.findViewById<CheckBox>(R.id.radioButton_gluten_free).isChecked) {
                mood + getString(R.string.gluten_free)
            }
            if (view.findViewById<CheckBox>(R.id.radioButton_no_restrictions).isChecked) {
                mood + getString(R.string.no_restrictions)
            }
        }


            // Write to SharedPreferences
            with(sharedPref.edit()) {
                putString("selected_dietary_restriction", mood)
                commit()
            }


            findNavController().navigate(R.id.action_dietRestrictionsFragment_to_cuisineFragment)

        backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}
