package com.bzolyomi.shoppinglist.ui.theme

import android.app.Activity
import android.graphics.Color.toArgb
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = Accent,
    primaryVariant = Purple700,
    secondary = Accent,
    onPrimary = Color.White,
    onSecondary = Color.White,
    background = Color.Black
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
    onError = Thrash,
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
        window.statusBarColor = LightPrimary.toArgb()
        window.navigationBarColor = LightPrimary.toArgb()
        systemUiController.setStatusBarColor(color = LightPrimary, darkIcons = true)
        systemUiController.setNavigationBarColor(color = LightPrimary, darkIcons = true)
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