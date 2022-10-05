package com.bzolyomi.shoppinglist.util

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bzolyomi.shoppinglist.ui.screens.AddAllScreen
import com.bzolyomi.shoppinglist.ui.screens.AllGroupsScreen
import com.bzolyomi.shoppinglist.ui.screens.ItemsOfGroupScreen
import com.bzolyomi.shoppinglist.ui.theme.GradientBackground
import com.bzolyomi.shoppinglist.viewmodels.SharedViewModel
import kotlinx.coroutines.runBlocking

@Composable
fun NavigationController(sharedViewModel: SharedViewModel, modifier: Modifier) {

    val navController: NavHostController = rememberNavController()

    val isInDarkMode = isSystemInDarkTheme()
    val backgroundModifier: Modifier =
        if (isInDarkMode) Modifier.background(Color.Black)
        else Modifier.background(GradientBackground)

    NavHost(navController = navController, startDestination = "home") {

        composable("home") {
            val shoppingGroupsWithLists by sharedViewModel.shoppingGroupsWithLists.collectAsState()

            AllGroupsScreen(
                shoppingGroupsWithLists = shoppingGroupsWithLists,
                onAddAllFABClicked = { navController.navigate("add") },
                onOpenGroupIconClicked = { groupId ->
                    navController.navigate("group/$groupId")
                    sharedViewModel.setCurrentGroupID(groupId = groupId)
                },
                onDeleteAllClicked = {
                    for (groupWithList in shoppingGroupsWithLists) {
                        sharedViewModel.deleteGroup(groupId = groupWithList.group.groupId)
                    }
                },
                modifier = backgroundModifier
                    .fillMaxSize()
            )
        }

        composable("add") {
            var isInputError: Boolean = false

            AddAllScreen(
                groupName = sharedViewModel.groupName,
                itemName = sharedViewModel.itemName,
                itemQuantity = sharedViewModel.itemQuantity,
                itemUnit = sharedViewModel.itemUnit,
                onGroupNameChange = { sharedViewModel.groupName = it },
                onItemNameChange = { sharedViewModel.itemName = it },
                onItemQuantityChange = { sharedViewModel.itemQuantity = it },
                onItemUnitChange = { sharedViewModel.itemUnit = it },
                onAddItemButtonClicked = { sharedViewModel.addItemFromGUIToItemList() },
                onSubmitAddAllButtonClicked = {
                    if (!isInputError) {
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                        sharedViewModel.createWithCoroutines()
                    }
                },
                onEraseGroupNameInputButtonClicked = { sharedViewModel.groupName = "" },
                onEraseItemNameInputButtonClicked = { sharedViewModel.itemName = "" },
                onEraseItemQuantityInputButtonClicked = { sharedViewModel.itemQuantity = "" },
                onEraseItemUnitInputButtonClicked = { sharedViewModel.itemUnit = "" },
                modifier = backgroundModifier
                    .fillMaxSize()
            )
        }

        composable(
            route = "group/{groupId}",
            arguments = listOf(navArgument("groupId") {
                type = NavType.StringType
            })
        ) { navBackStackEntry ->
            val groupId = navBackStackEntry.arguments!!.getString("groupId")
//            var selectedGroupWithList: GroupWithList = GroupWithList(
//                ShoppingGroupEntity(
//                    0, ""
//                ),
//                shoppingList = emptyList()
//            )

            LaunchedEffect(key1 = groupId) {
                sharedViewModel.getSelectedGroupWithList(groupId = groupId)
//                withContext(this.coroutineContext) {
//                    sharedViewModel.selectedGroupWithList.collect {
//                        selectedGroupWithList = it
//                    }
//                }
            }
            val selectedGroupWithList by sharedViewModel.selectedGroupWithList.collectAsState()

            ItemsOfGroupScreen(
                selectedGroupWithList = selectedGroupWithList,
                onDeleteItemClicked = {
                    sharedViewModel.deleteItem(itemId = it)
                },
                onDeleteGroupClicked = { groupIdToDelete, shoppingListToDelete ->
                    sharedViewModel.setCurrentGroupID(groupId = null)
                    sharedViewModel.deleteGroup(groupId = groupIdToDelete)
                    sharedViewModel.deleteItems(shoppingListToDelete)
                    navController.navigate("home")
                },
                itemName = sharedViewModel.itemName,
                itemQuantity = sharedViewModel.itemQuantity,
                itemUnit = sharedViewModel.itemUnit,
                onItemNameChange = { sharedViewModel.itemName = it },
                onItemQuantityChange = { sharedViewModel.itemQuantity = it },
                onItemUnitChange = { sharedViewModel.itemUnit = it },
                onSubmitAddItemButtonClicked = {
                    runBlocking { sharedViewModel.createItems(groupId = it) }
                },
                onCheckboxClicked = {
                    sharedViewModel.updateItemChecked(it)
                },
                onEraseItemNameInputButtonClicked = {
                    sharedViewModel.itemName = ""
                },
                onEraseItemQuantityInputButtonClicked = {
                    sharedViewModel.itemQuantity = ""
                },
                onEraseItemUnitInputButtonClicked = {
                    sharedViewModel.itemUnit = ""
                },
                onCancelAddItemButtonClicked = {
                    sharedViewModel.flushItemGUI()
                },
                modifier = backgroundModifier
                    .fillMaxSize()
//                sharedViewModel = sharedViewModel,
//                onItemsRearrangedOnGUI = {
//                    sharedViewModel.rearrangeItems(selectedGroupWithList.shoppingList, it)
//                }
            )
        }
    }
}
