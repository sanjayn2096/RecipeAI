package com.sunj.recipeai.ui

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sunj.recipeai.R

@Composable
fun BackButton(onClick: () -> Unit, modifier: Modifier) {
   return  IconButton(
        onClick = { onClick() },
        modifier = modifier.size(30.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.angle_circle_left),
            contentDescription = "Back",
            tint = Color.Unspecified
        )
    }
}

@Composable
fun appLogo ( modifier: Modifier ) {
    Text(
        text = stringResource(id = R.string.recipeai),
        fontSize = 36.sp,
        fontFamily = FontFamily(Font(R.font.titan_one)),
        color = colorResource(id = R.color.custom_green),
        textAlign = TextAlign.Center,
        modifier = modifier,
        style = TextStyle(
            shadow = Shadow(
                color = colorResource(id = R.color.light_gray),
                offset = Offset(0f, 7f),
                blurRadius = 1f
            )
        )
    )
}
