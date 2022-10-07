package com.bzolyomi.shoppinglist.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
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

    val view = LocalView.current
    val window = (view.context as Activity).window
    val systemUiController = rememberSystemUiController()

    if (darkTheme) {
        window.statusBarColor = Color.Black.toArgb()
    } else {
//        window.statusBarColor = Primary.toArgb()
//        window.navigationBarColor = LightPrimary.toArgb()
        systemUiController.setStatusBarColor(color = LightSecondary, darkIcons = true)
        systemUiController.setNavigationBarColor(color = LightSecondary, darkIcons = true)
    }

//    if (!view.isInEditMode) {
//        SideEffect {
//            val window = (view.context as Activity).window
//            val insets = WindowCompat.getInsetsController(window, view)
//            window.statusBarColor = Color.Transparent.toArgb() // choose a status bar color
//            window.navigationBarColor = Color.Transparent.toArgb() // choose a navigation bar color
//            insets.isAppearanceLightStatusBars = !darkTheme
//            insets.isAppearanceLightNavigationBars = !darkTheme
//        }
//    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

@Composable
fun IntroTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    val view = LocalView.current
    val window = (view.context as Activity).window
    val systemUiController = rememberSystemUiController()

    if (darkTheme) {
        window.statusBarColor = Color.Black.toArgb()
    } else {
//        window.statusBarColor = Primary.toArgb()
//        window.navigationBarColor = LightPrimary.toArgb()
        systemUiController.setStatusBarColor(color = Color.White, darkIcons = true)
        systemUiController.setNavigationBarColor(color = Color.White, darkIcons = true)
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}