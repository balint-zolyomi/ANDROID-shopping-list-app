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
            val shoppingGroupsWithLists by sharedViewModel.shoppingGroupsWithLists.collectAsState()

            AllGroupsScreen(
                shoppingGroupsWithLists = shoppingGroupsWithLists,
                onAddAllFABClicked = { navController.navigate("add") },
                onOpenGroupIconClicked = { groupId ->
                    navController.navigate("group/$groupId")
                    sharedViewModel.setCurrentGroupID(groupId = groupId)
                }
            )
        }

        composable("add") {
            AddAllScreen(
                groupName = sharedViewModel.groupName,
                itemName = sharedViewModel.itemName,
                itemQuantity = sharedViewModel.itemQuantity,
                itemUnit = sharedViewModel.itemUnit,
                onGroupNameChange = { sharedViewModel.groupName = it },
                onItemNameChange = { sharedViewModel.itemName = it },
                onItemQuantityChange = { sharedViewModel.itemQuantity = it },
                onItemUnitChange = { sharedViewModel.itemUnit = it },
//                onAddItemButtonClicked = { sharedViewModel.addToItemList() },
                onAddItemButtonClicked = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onSubmitAddAllButtonClicked = {
                    sharedViewModel.createWithCoroutines()
                }
            )
        }

//        composable(
//            route = "group/{groupId}",
//            arguments = listOf(navArgument("groupId") {
//                type = NavType.StringType
//            })
//        ) { navBackStackEntry ->
//            val groupId = navBackStackEntry.arguments!!.getString("groupId")
////            var selectedGroupWithList: GroupWithList = GroupWithList(
////                ShoppingGroupEntity(
////                    0, ""
////                ),
////                shoppingList = emptyList()
////            )
//
//            LaunchedEffect(key1 = groupId) {
//                sharedViewModel.getSelectedGroupWithList(groupId = groupId)
////                withContext(this.coroutineContext) {
////                    sharedViewModel.selectedGroupWithList.collect {
////                        selectedGroupWithList = it
////                    }
////                }
//            }
//            val selectedGroupWithList by sharedViewModel.selectedGroupWithList.collectAsState()
//
//            ItemsOfGroupScreen(
//                selectedGroupWithList = selectedGroupWithList,
//                onDeleteItemClicked = {
//                    sharedViewModel.deleteItem(itemId = it)
//                },
//                onDeleteGroupClicked = { groupId, shoppingList ->
//                    sharedViewModel.deleteGroupCache(groupId = groupId)
//                    sharedViewModel.deleteItems(shoppingList)
//                    navController.navigate("home")
//                },
//                itemName = sharedViewModel.itemName,
//                itemQuantity = sharedViewModel.itemQuantity,
//                itemUnit = sharedViewModel.itemUnit,
//                isItemChecked = sharedViewModel.isItemChecked,
//                onItemNameChange = { sharedViewModel.itemName = it },
//                onItemQuantityChange = { sharedViewModel.itemQuantity = it },
//                onItemUnitChange = { sharedViewModel.itemUnit = it },
//                onSubmitAddItemButtonClicked = {
////                    sharedViewModel.createItemsAndClearCaches()
//                },
//                onCheckboxClicked = { sharedViewModel.updateItemChecked(it) },
//                sharedViewModel = sharedViewModel,
//                onItemsRearrangedOnGUI = {
////                    sharedViewModel.rearrangeItems(selectedGroupWithList.shoppingList, it)
//                }
//            )
//        }
    }
}
