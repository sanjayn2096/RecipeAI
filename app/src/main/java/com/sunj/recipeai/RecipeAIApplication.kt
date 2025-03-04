package com.sunj.recipeai

import android.app.Application
import com.google.firebase.FirebaseApp

class RecipeAIApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
