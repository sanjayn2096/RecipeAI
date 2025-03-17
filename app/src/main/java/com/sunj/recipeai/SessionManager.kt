package com.sunj.recipeai

import android.content.Context
import android.content.SharedPreferences
import com.sunj.recipeai.screens.GeneratorScreen

class SessionManager(context: Context) {
    private var prefs: SharedPreferences =
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun saveSession(sessionId: String) {
        val editor = prefs.edit()
        editor.putString("SESSION_ID", sessionId)
        editor.apply()
    }

    fun getSession(): String? {
        return prefs.getString("SESSION_ID", null)
    }

    fun clearSession() {
        val editor = prefs.edit()
        editor.remove("SESSION_ID")
        editor.apply()
    }

    fun saveUserId(userId : String) {
        val editor = prefs.edit()
        editor.putString("USER_ID", userId)
        editor.apply()
    }

    fun getUserId() : String? {
        return prefs.getString("USER_ID", null)
    }

    fun saveEmail(email: String) {
        prefs.edit().putString("email", email).apply()
    }

    fun getEmail(): String? {
        return prefs.getString("email", "john.doe@example.com")
    }

    private fun saveMood(mood: String) {
        prefs.edit().putString(GeneratorScreen.Mood.route, mood).apply()
    }

    fun getMood(): String? {
        return prefs.getString(GeneratorScreen.Mood.route, "lucky")
    }

    private fun saveCuisine(cuisine: String) {
        prefs.edit().putString(GeneratorScreen.CuisinePreference.route, cuisine).apply()
    }

    fun getCuisine(): String? {
        return prefs.getString(GeneratorScreen.CuisinePreference.route, "No Cuisine Selected")
    }

    private fun saveCookingPreference(cookingPreference: String) {
        prefs.edit().putString(GeneratorScreen.CookingPreferences.route, cookingPreference).apply()
    }

    fun getCookingPreference(): String? {
        return prefs.getString(GeneratorScreen.CookingPreferences.route, "No Cooking Preferences")
    }

    private fun saveDietRestrictions(dietRestrictions: String) {
        prefs.edit().putString(GeneratorScreen.DietRestrictions.route, dietRestrictions).apply()
    }

    fun savePreference(title: String, selectedOption: String) {
        // switch case
        when (title) {
            GeneratorScreen.Mood.route -> saveMood(selectedOption)
            GeneratorScreen.DietRestrictions.route -> saveDietRestrictions(selectedOption)
            GeneratorScreen.CuisinePreference.route -> saveCuisine(selectedOption)
            GeneratorScreen.CookingPreferences.route -> saveCookingPreference(selectedOption)
            "customPreference" -> saveCustomPreference(selectedOption)
        }
    }

    private fun saveCustomPreference(customText: String) {
        prefs.edit().putString("customPreference", customText).apply()
    }

    fun getPreference(title: String): String? {
        return when (title) {
            GeneratorScreen.Mood.route -> prefs.getString(GeneratorScreen.Mood.route, "lucky")
            GeneratorScreen.DietRestrictions.route -> prefs.getString(GeneratorScreen.DietRestrictions.route, "No Diet Restrictions")
            GeneratorScreen.CookingPreferences.route -> prefs.getString(GeneratorScreen.CookingPreferences.route, "No Cooking Preferences")
            GeneratorScreen.CuisinePreference.route -> prefs.getString(GeneratorScreen.CuisinePreference.route, "No Cuisine Selected")
            "customPreference" -> prefs.getString("customPreference", "")
            else -> null
        }
    }

    fun getDietRestrictions(): String? {
        return prefs.getString(GeneratorScreen.DietRestrictions.route, "No Diet Restrictions")
    }
}
