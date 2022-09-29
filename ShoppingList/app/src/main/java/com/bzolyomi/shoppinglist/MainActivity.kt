package com.bzolyomi.shoppinglist

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.bzolyomi.shoppinglist.ui.theme.GradientBackground
import com.bzolyomi.shoppinglist.ui.theme.ShoppingListTheme
import com.bzolyomi.shoppinglist.util.NavigationController
import com.bzolyomi.shoppinglist.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, true)

        setContent {
            ShoppingListTheme {
                NavigationController(
                    sharedViewModel = sharedViewModel,
                    modifier = Modifier.fillMaxSize().background(GradientBackground)
                )
            }
        }
    }
}