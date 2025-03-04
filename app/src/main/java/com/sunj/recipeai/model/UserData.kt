package com.sunj.recipeai.model

data class UserData (
    val email : String,
    var firstName: String,
    val lastName: String?,
    val favorite_recipes: List<Recipes>?,
    val created_recipes: List<String>?
)