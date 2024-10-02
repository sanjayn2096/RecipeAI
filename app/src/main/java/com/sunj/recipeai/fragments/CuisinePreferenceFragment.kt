package com.sunj.recipeai.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sunj.recipeai.R

class CuisinePreferenceFragment : Fragment(R.layout.fragment_cuisine_preference) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get SharedPreferences instance
        val sharedPref: SharedPreferences =
            requireContext().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)

        // Find the button by its ID
        val nextButton: Button = view.findViewById(R.id.Button_next_cuisine_preferences)
        val backButton: ImageButton = view.findViewById(R.id.back_button_cuisine_preferences)

        // Set a click listener for the button
        nextButton.setOnClickListener {
            // Check which RadioButton is selected
            val mood: String = when {
                view.findViewById<RadioButton>(R.id.radioButton_cuisine_1).isChecked -> getString(R.string.indian_cuisine)
                view.findViewById<RadioButton>(R.id.radioButton_cuisine_2).isChecked -> getString(R.string.mexican)
                view.findViewById<RadioButton>(R.id.radioButton_cuisine_3).isChecked -> getString(R.string.chinese)
                view.findViewById<RadioButton>(R.id.radioButton_cuisine_4).isChecked -> getString(R.string.thai)
                view.findViewById<RadioButton>(R.id.radioButton_cuisine_5).isChecked -> getString(R.string.korean)
                view.findViewById<RadioButton>(R.id.radioButton_cuisine_6).isChecked -> getString(R.string.surprise_me_with_anything)
                else -> "Any Cuisine"
            }

            // Write to SharedPreferences
            with(sharedPref.edit()) {
                putString("selected_cuisine_preference", mood)
                commit()
            }

            // Optional: Show a confirmation toast
            Toast.makeText(requireContext(), "cuisine saved: $mood", Toast.LENGTH_SHORT).show()

            findNavController().navigate(R.id.action_cuisinePreferenceFragment_to_cookingPreferencesFragment)
        }

        backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStackImmediate()
        }
    }
}
