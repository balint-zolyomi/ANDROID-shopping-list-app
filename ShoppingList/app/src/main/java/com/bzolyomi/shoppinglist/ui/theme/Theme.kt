package com.bzolyomi.shoppinglist.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = Primary,
    secondary = Secondary,
    onPrimary = TextOnDark,
    background = Color.Black
)

private val LightColorPalette = lightColors(
    primary = Primary,
    secondary = Secondary,
//     Other default colors to override
    background = LightSecondary,
    surface = Color.White,
    onPrimary = OnPrimary,
    onSecondary = Text,
    onBackground = Text,
    onSurface = Text
)

@Composable
fun ShoppingListTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    val systemUiController = rememberSystemUiController()

    if (darkTheme) {
        systemUiController.setStatusBarColor(color = Color.Black, darkIcons = false)
    } else {
        systemUiController.setStatusBarColor(color = LightSecondary, darkIcons = true)
        systemUiController.setNavigationBarColor(color = LightSecondary, darkIcons = true)
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}