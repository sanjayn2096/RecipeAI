package com.sunj.recipeai.experiments

import android.location.Geocoder.GeocodeListener
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sunj.recipeai.R
import com.sunj.recipeai.model.Recipes


//@Composable
//fun displayRecipe(recipe: Recipes) {
//    Card {
//        Row {
//            Image(
//                painter = painterResource(id = R.drawable.food_pic), // Load image
//                contentDescription = "Recipe Image",                  // Accessibility description
//                modifier = Modifier.size(100.dp)                       // Image size
//            )
//            Spacer(modifier = Modifier.width(50.dp))
//            Column(
//                modifier = Modifier.padding(16.dp)
//            ) {
//                Text(text = recipe.recipeName, style = MaterialTheme.typography.bodyMedium)
//                Text(text = recipe.cookingTime, style = MaterialTheme.typography.bodyMedium)
//                Text(text = recipe.instructions, style = MaterialTheme.typography.bodyMedium)
//            }
//
//        }
//    }
//}
