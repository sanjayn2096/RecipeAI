package com.sunj.recipeai.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NutritionalValue(
    val calories: String,
    val protein: String,
    val carbs: String,
    val fat: String,
    val vitamins: String,
    val numberOfServings: Int
) : Parcelable {
}