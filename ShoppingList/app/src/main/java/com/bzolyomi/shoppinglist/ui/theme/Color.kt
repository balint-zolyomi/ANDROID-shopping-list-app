package com.bzolyomi.shoppinglist.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Primary = Color(0xFFE040FB)
val OnPrimary = Color(0xFFFEFEFE)
val LightSecondary = Color(0xFFDCEDC8)
//val DarkSecondary = Color(0xFF689F38)
val Secondary = Color(0xFF8BC34A)
val Text = Color(0xFF212121)
val TextOnDark = Color(0xFFDEDEDE)

val FloatingActionButtonTint = Color.White

val GradientBackground = Brush.linearGradient(
    colors = listOf(LightSecondary, Secondary),
    start = Offset(0f, 0f),
    end = Offset(0f, Float.POSITIVE_INFINITY)
)