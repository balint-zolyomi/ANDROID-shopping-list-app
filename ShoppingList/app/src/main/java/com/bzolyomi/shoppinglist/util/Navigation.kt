package com.bzolyomi.shoppinglist.util

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bzolyomi.shoppinglist.ui.screens.AddAllScreen
import com.bzolyomi.shoppinglist.ui.screens.ItemGroupScreen
import com.bzolyomi.shoppinglist.viewmodels.SharedViewModel

@Composable
fun NavigationController(sharedViewModel: SharedViewModel) {

    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            ItemGroupScreen(
                sharedViewModel = sharedViewModel,
                onNavigateToAddAllScreen = { navController.navigate("add") }
            )
        }
        composable("add") {
            AddAllScreen(
                sharedViewModel = sharedViewModel
            )
        }
    }
}
