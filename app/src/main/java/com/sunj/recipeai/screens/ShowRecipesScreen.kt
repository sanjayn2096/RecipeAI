package com.sunj.recipeai.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sunj.recipeai.ui.BackButton
import com.sunj.recipeai.R
import com.sunj.recipeai.model.Recipes

@Composable
fun ShowRecipe(
    recipe: Recipes,
    onClose: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        // Close Button
        BackButton(
            onClick = { onClose() },
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Recipe Name
        Text(
            text = recipe.recipeName,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Recipe Image
        Image(
            painter = painterResource(R.drawable.food_pic),
            contentDescription = "Recipe Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.LightGray)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Nutrition Info Section
        NutritionInfo(
            cookTime = recipe.cookingTime,
            servings = recipe.nutritionalValue.numberOfServings,
            calories = recipe.nutritionalValue.calories,
            protein =  recipe.nutritionalValue.protein,
            carbs = recipe.nutritionalValue.carbs,
            fat = recipe.nutritionalValue.fat,
            vitamins = recipe.nutritionalValue.vitamins
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Ingredients Header
        Text(
            text = "Ingredients",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Recipe Ingredients
        Text(
            text = recipe.ingredients,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Instructions Header
        Text(
            text = "Instructions",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Recipe Instructions
        Text(
            text = recipe.instructions,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun NutritionInfo(
    cookTime: String,
    servings: Int,
    calories: String,
    protein: String,
    carbs: String,
    fat: String,
    vitamins: String
) {
    val horizontalScrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF8F8F8))
            .horizontalScroll(horizontalScrollState)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        NutritionItem("Cook Time", label = cookTime)
        NutritionItem("Servings", label = servings.toString())
        NutritionItem("Calories", label = calories)
        NutritionItem("Protein", label = protein)
        NutritionItem("Carbs", label = carbs)
        NutritionItem("Fat", label = fat)
        NutritionItem("Vitamins", label = vitamins)
    }
}

@Composable
fun NutritionItem(title : String, label: String) {
    Column(modifier = Modifier.width(100.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            maxLines = 2,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            maxLines = 2,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Light
        )
    }
}


@Preview
@Composable
fun previewNutritionBar() {
    NutritionInfo("20 Minutes",
        2,"200kcal", "20g", "20g", "20g", "20g")
}

