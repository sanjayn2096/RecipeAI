package com.sunj.recipeai.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.sunj.recipeai.ui.BackButton
import com.sunj.recipeai.R
import com.sunj.recipeai.SessionManager
import com.sunj.recipeai.activities.RecipeActivity
import com.sunj.recipeai.model.UserData
import com.sunj.recipeai.model.optionMapping


sealed class GeneratorScreen(val route: String) {
    object Mood : GeneratorScreen("mood")
    object DietRestrictions : GeneratorScreen("dietRestrictions")
    object CuisinePreference : GeneratorScreen("cuisinePreferences")
    object CookingPreferences : GeneratorScreen("cookingPreferences")
    object RecipeActivity: GeneratorScreen("recipeActivity")
}

val screenMapping : Map<String, String> = mapOf(
    "mood" to "dietRestrictions",
    "dietRestrictions" to "cuisinePreferences",
    "cuisinePreferences" to "cookingPreferences",
    "cookingPreferences" to "recipeActivity"
)
val titleMapping : Map<String, Int> = mapOf(
    "mood" to R.string.how_are_you_feeling_today,
    "dietRestrictions" to R.string.do_you_have_any_dietary_restrictions,
    "cuisinePreferences" to R.string.what_cuisine_do_you_feel_like_eating_today,
    "cookingPreferences" to R.string.how_much_time_do_you_like_to_spend_on_cooking
)

fun nextScreen(navController: NavController, route: String, selectedOption : String?)  {
    if(route == "mood")  {
        if(selectedOption == navController.context.getString(R.string.i_m_feeling_lucky)){
            navController.navigate("recipeActivity")
        } else {
            navController.navigate("dietRestrictions")
        }
    } else {
        val next = screenMapping[route]!!
        navController.navigate(next)
    }
}

@Composable
fun RecipeActivityScreen(navController: NavHostController, userData: UserData?) {
    val context = navController.context
    val userDataJson = Gson().toJson(userData)

    context.startActivity(Intent(context, RecipeActivity::class.java).apply {
        putExtra("userData", userDataJson)
    })
}


@Composable
fun PromptScreen(navController: NavController, title : String) {
    var selectedOption by rememberSaveable { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val options = optionMapping[title]

    Box(
        modifier = Modifier.background(Color.White)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Back Button
            BackButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.Start)
            )

            Text(
                text = stringResource(titleMapping[title]!!),
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.source_sans_pro)),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Radio Group Equivalent
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                options!!.forEach { option ->
                    val stringOption = stringResource(option)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable { selectedOption = stringOption },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedOption == stringOption,
                            onClick = { selectedOption = stringOption }
                        )
                        Text(
                            text = stringOption,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.source_sans_pro)),
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Next Button
            Button(
                onClick = {
                    selectedOption?.let {
                        SessionManager(context).savePreference(title, it)
                        nextScreen(navController, title, selectedOption)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.custom_green))
            ) {
                Text(
                    text = stringResource(R.string.next),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

