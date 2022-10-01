package com.bzolyomi.shoppinglist.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

val LightGrey = Color(0xFFBBBBBB)

val LightYellow = Color(0xFFFEF5AC)
val LightBlue = Color(0xFF97D2EC)
val DarkGray = Color(0xFF5F6F94)
val DarkBlue = Color(0xFF25316D)

val Sand = Color(0xFFF0EABE)
val Turquoise = Color(0xFF21E1E1)
val Sky = Color(0xFF3B9AE1)
val Water = Color(0xFF3120E0)

val Primary = Color(0xFFE040FB)
val OnPrimary = Color(0xFFFEFEFE)
val LightSecondary = Color(0xFFDCEDC8)
val DarkSecondary = Color(0xFF689F38)
val Accent = Color(0xFFE040FB)
val Secondary = Color(0xFF8BC34A)
val Text = Color(0xFF212121)
val TextOnDark = Color(0xFFDEDEDE)
val SecondaryText = Color(0xFF757575)
val Divider = Color(0xFFBDBDBD)
val FloatingActionButtonTint = Color.White

val GradientBackground = Brush.linearGradient(
    colors = listOf(LightSecondary, Secondary),
    start = Offset(0f, 0f),
    end = Offset(0f, Float.POSITIVE_INFINITY)
)