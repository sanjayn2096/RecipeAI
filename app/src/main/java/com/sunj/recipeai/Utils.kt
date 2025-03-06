package com.sunj.recipeai

import java.util.UUID

fun generateRandomId(): String {
    return UUID.randomUUID().toString() // Unique session ID
}