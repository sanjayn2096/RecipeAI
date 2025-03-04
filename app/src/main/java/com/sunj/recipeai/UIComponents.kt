package com.sunj.recipeai

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

@Composable
fun BackButton(onClick: () -> Unit, modifier: Modifier) {
   return  IconButton(
        onClick = { onClick() },
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(id = R.drawable.angle_circle_left),
            contentDescription = "Back",
            tint = Color.Unspecified
        )
    }
}
