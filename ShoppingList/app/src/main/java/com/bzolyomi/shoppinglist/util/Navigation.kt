package com.bzolyomi.shoppinglist.util

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
                onNavigateToItemsOfGroupScreen = { groupId ->
                    navController.navigate("group/$groupId")
                }
            )
        }
        composable("add") {
            AddAllScreen(
                sharedViewModel = sharedViewModel,
                onNavigateToAllGroupsScreen = {
                    sharedViewModel.createGroupAndItems()
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = "group/{groupId}",
            arguments = listOf(navArgument("groupId") {
                type = NavType.StringType
            })
        ) { navBackStackEntry ->
            val groupId = navBackStackEntry.arguments!!.getString("groupId")

            LaunchedEffect(key1 = groupId) {
                sharedViewModel.getSelectedGroupWithList(groupId = groupId)
            }
            val selectedGroupWithList by sharedViewModel.selectedGroupWithList.collectAsState()

            ItemsOfGroupScreen(
                selectedGroupWithList = selectedGroupWithList,
                onDeleteItemClicked = {
                    sharedViewModel.deleteItem(itemId = it)
                },
                onDeleteGroupClicked = { groupId, shoppingList ->
                    sharedViewModel.deleteGroup(groupId = groupId)
                    sharedViewModel.deleteItems(shoppingList)
                    navController.navigate("home")
                },
                itemName = sharedViewModel.itemName,
                itemQuantity = sharedViewModel.itemQuantity,
                itemUnit = sharedViewModel.itemUnit,
                onItemNameChange = { sharedViewModel.itemName = it },
                onItemQuantityChange = { sharedViewModel.itemQuantity = it },
                onItemUnitChange = { sharedViewModel.itemUnit = it },
                onSubmitButtonClicked = {
                    sharedViewModel.createItems(groupId = it)
                }
            )
        }
    }
}
