package com.sunj.recipeai.model

import com.sunj.recipeai.R

val moodOptions: List<Int> = listOf((R.string.happy_excited),
    R.string.sad_tired,
    R.string.not_hungry,
    R.string.neutral,
    R.string.i_m_feeling_lucky)

val dietaryRestrictionsOptions: List<Int> = listOf(
    R.string.vegetarian,
    R.string.vegan,
    R.string.pescitarian,
    R.string.non_vegetarian_without_red_meat,
    R.string.gluten_free,
    R.string.no_restrictions)

val cuisinePreferenceOptions: List<Int> = listOf(
    R.string.indian_cuisine,
    R.string.mexican,
    R.string.chinese,
    R.string.thai,
    R.string.korean,
)
val cookingPreferencesOptions: List<Int> = listOf(
    R.string._10_minutes,
    R.string._10_30_minutes,
    R.string._30_60_minutes,
    R.string._60_minutes
)

val optionMapping: Map<String, List<Int>> = mapOf(
    "mood" to moodOptions,
    "dietRestrictions" to dietaryRestrictionsOptions,
    "cuisinePreferences" to cuisinePreferenceOptions,
    "cookingPreferences" to cookingPreferencesOptions
)