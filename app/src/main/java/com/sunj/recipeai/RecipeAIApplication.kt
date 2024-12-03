package com.sunj.recipeai

import android.app.Application
import android.content.Intent
import com.sunj.recipeai.activities.MainActivity

class RecipeAIApplication : Application() {
    override fun onCreate() {
        super.onCreate()
            this.startActivity(Intent(applicationContext, MainActivity::class.java))
    }
}