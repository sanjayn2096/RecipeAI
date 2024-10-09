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

class MoodFragment : Fragment(R.layout.fragment_mood) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get SharedPreferences instance
        val sharedPref: SharedPreferences =
            requireContext().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)

        // Find the button by its ID
        val nextButton: Button = view.findViewById(R.id.Button_next_mood)
        nextButton.setBackgroundResource(R.drawable.button_resource);
        val backButton: ImageButton = view.findViewById(R.id.back_button_mood)

        // Set a click listener for the button
        nextButton.setOnClickListener {
            // Check which RadioButton is selected
            val mood: String = when {
                view.findViewById<RadioButton>(R.id.radioButton).isChecked -> getString(R.string.happy_excited)
                view.findViewById<RadioButton>(R.id.radioButton2).isChecked -> getString(R.string.sad_tired)
                view.findViewById<RadioButton>(R.id.radioButton3).isChecked -> getString(R.string.not_hungry)
                view.findViewById<RadioButton>(R.id.radioButton4).isChecked -> getString(R.string.neutral)
                else -> "No mood selected"
            }

            // Write to SharedPreferences
            with(sharedPref.edit()) {
                putString("selected_mood", mood)
                apply()  // Save the changes asynchronously (use commit() if you need immediate saving)
            }

            findNavController().navigate(R.id.action_moodFragment_to_dietRestrictionsFragment)
        }

        backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}
