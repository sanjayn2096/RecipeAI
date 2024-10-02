package com.sunj.recipeai.fragments

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sunj.recipeai.R

class LandingFragment : Fragment(R.layout.fragment_main) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainScreen: ConstraintLayout = view.findViewById(R.id.fragment_main)

        // Set a click listener on the LinearLayout
        mainScreen.setOnClickListener {
            findNavController().navigate(R.id.action_landingFragment_to_moodFragment)
        }
    }
}