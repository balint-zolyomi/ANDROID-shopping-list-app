package com.bzolyomi.shoppinglist.util

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bzolyomi.shoppinglist.ui.screens.AddAllScreen
import com.bzolyomi.shoppinglist.ui.screens.AllGroupsScreen
import com.bzolyomi.shoppinglist.ui.screens.ItemsOfGroupScreen
import com.bzolyomi.shoppinglist.viewmodels.SharedViewModel

@Composable
fun NavigationController(sharedViewModel: SharedViewModel) {

    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            AllGroupsScreen(
                sharedViewModel = sharedViewModel,
                onNavigateToAddAllScreen = { navController.navigate("add") },
                onNavigateToItemsOfGroupScreen = { navController.navigate("group")}
            )
        }
        composable("add") {
            AddAllScreen(
                sharedViewModel = sharedViewModel,
                onNavigateToItemGroupScreen = {
                    sharedViewModel.createGroupAndItems()
                    navController.navigate("home")
                }
            )
        }
        composable("group") {
            ItemsOfGroupScreen(sharedViewModel = sharedViewModel)
        }
    }
}
