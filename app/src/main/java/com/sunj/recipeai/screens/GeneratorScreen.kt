package com.sunj.recipeai.screens

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.gson.Gson
import com.sunj.recipeai.BackButton
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

//@Composable
//fun EditPreferenceScreen() {
//    val menuExpanded = remember { mutableStateOf(false) }
//    Box(
//        modifier = Modifier.paint(
//        painter = painterResource(id = R.drawable.bg_regular),
//        contentScale = ContentScale.Crop)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp)
//                .verticalScroll(rememberScrollState()),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            BackButton(
//                onClick = { /*navController.popBackStack()*/ },
//                modifier = Modifier.align(Alignment.Start)
//            )
//
//            Text(
//                text = "Edit Your Preferences",
//                fontSize = 24.sp,
//                fontFamily = FontFamily(Font(R.font.source_sans_pro)),
//                textAlign = TextAlign.Center,
//                modifier = Modifier.padding(vertical = 16.dp)
//            )
//            Box {
//                Row {
//                    Column {
//                        Text(
//                            modifier = Modifier.align(Alignment.CenterHorizontally),
//                            text = "Mood Preference",
//
//                        )
//                        Spacer(modifier = Modifier.size(5.dp))
//                        Text(
//                            modifier = Modifier.align(Alignment.CenterHorizontally),
//                            text = SessionManager(LocalContext.current).getMood()!!
//
//                        )
//                    }
//
//                    IconButton(onClick = { menuExpanded.value = true }) {
//                        Icon(
//                            imageVector = Icons.Filled.Mood, contentDescription = "Menu",
//                            tint = MaterialTheme.colorScheme.onSecondary
//                        )
//
//                    }
//                    DropdownMenu(
//                        expanded = menuExpanded.value,
//                        onDismissRequest = { menuExpanded.value = false }
//                    ) {
//                        DropdownMenuItem(text = { Text("Help") }, onClick = { /* TODO: Handle Help */ })
//                        DropdownMenuItem(
//                            text = { Text("Tutorial") },
//                            onClick = { /* TODO: Handle Tutorial */ })
//                        DropdownMenuItem(text = { Text("Tips") }, onClick = { /* TODO: Handle Tips */ })
//                        DropdownMenuItem(text = { Text("Logout") }, onClick = { })
//                    }
//                }
//
//
//            }
//
//        }
//    }
//}


@Composable
fun EditPreferenceScreen() {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    val preferences = listOf(
        GeneratorScreen.Mood.route,
        GeneratorScreen.CuisinePreference.route,
        GeneratorScreen.DietRestrictions.route,
        GeneratorScreen.CookingPreferences.route,
        )

    val selectedOptions = remember { mutableStateMapOf<String, String>() }
    val customInput = remember { mutableStateMapOf<String, String>() }

    val menuExpanded = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.paint(
            painter = painterResource(id = R.drawable.bg_regular),
            contentScale = ContentScale.Crop
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BackButton(
                onClick = { /*navController.popBackStack()*/ },
                modifier = Modifier.align(Alignment.Start)
            )

            Text(
                text = "Edit Your Preferences",
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.source_sans_pro)),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            preferences.forEach { preference ->
                val options = optionMapping[preference.lowercase()]?.map { stringResource(it) } ?: emptyList()

                var expanded by remember { mutableStateOf(false) }
                var selectedOption by remember { mutableStateOf(sessionManager.getPreference(preference) ?: "") }
                var showCustomInput by remember { mutableStateOf(false) }

                Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Text(text = preference, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(4.dp))

                    Box(modifier = Modifier.fillMaxWidth().border(1.dp, Color.Gray, RoundedCornerShape(4.dp))) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable { expanded = true },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = selectedOption.ifEmpty { "Select an option" },
                                modifier = Modifier.weight(1f)
                            )
                            Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "Dropdown")
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            options.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        selectedOption = option
                                        expanded = false
                                        showCustomInput = option == "Other"
                                    }
                                )
                            }
                        }
                    }

                    if (showCustomInput) {
                        TextField(
                            value = customInput[preference] ?: "",
                            onValueChange = { customInput[preference] = it },
                            placeholder = { Text("Enter custom preference") },
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                        )
                    }
                }
            }

            Button(
                onClick = {
                    preferences.forEach { preference ->
                        val finalValue = if (customInput[preference].isNullOrEmpty()) {
                            selectedOptions[preference] ?: ""
                        } else {
                            customInput[preference]!!
                        }
                        sessionManager.savePreference(preference, finalValue)
                    }
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Set Preferences")
            }
        }
    }
}


@Composable
fun PromptScreen(navController: NavController, title : String) {
    var selectedOption by rememberSaveable { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val options = optionMapping[title]

    Box(
        modifier = Modifier.paint(
            painter = painterResource(id = R.drawable.bg_regular),
            contentScale = ContentScale.Crop)
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

@Preview(showBackground = true)
@Composable
fun PreviewEditScreen() {
    EditPreferenceScreen()
}
