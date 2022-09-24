package com.bzolyomi.shoppinglist.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Transparent
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = Primary,
    primaryVariant = Purple700,
    secondary = Accent,

)

private val LightColorPalette = lightColors(
    primary = Accent,
    primaryVariant = Primary,
    secondary = Accent,

//     Other default colors to override
    background = LightPrimary,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = TextIcons,
    onBackground = TextIcons,
    onSurface = TextIcons,
)

@Composable
fun ShoppingListTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    val systemUiController = rememberSystemUiController()
    if (darkTheme) {
        systemUiController.setSystemBarsColor(color = Color.Black)
    } else {
        systemUiController.setStatusBarColor(color = Primary)
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}