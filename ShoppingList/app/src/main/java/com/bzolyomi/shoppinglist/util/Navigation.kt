package com.bzolyomi.shoppinglist.util

import androidx.compose.animation.*
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
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
import com.google.accompanist.navigation.animation.composable
import androidx.navigation.navArgument
import com.bzolyomi.shoppinglist.ui.screens.AddAllScreen
import com.bzolyomi.shoppinglist.ui.screens.AllGroupsScreen
import com.bzolyomi.shoppinglist.ui.screens.IntroScreen
import com.bzolyomi.shoppinglist.ui.screens.ItemsOfGroupScreen
import com.bzolyomi.shoppinglist.ui.theme.GradientBackground
import com.bzolyomi.shoppinglist.ui.theme.IntroTheme
import com.bzolyomi.shoppinglist.ui.theme.ShoppingListTheme
import com.bzolyomi.shoppinglist.util.Constants.ADD_SCREEN_ENTER_DURATION
import com.bzolyomi.shoppinglist.util.Constants.ADD_SCREEN_EXIT_DURATION
import com.bzolyomi.shoppinglist.util.Constants.GROUP_SCREEN_ENTER_DURATION
import com.bzolyomi.shoppinglist.util.Constants.GROUP_SCREEN_EXIT_DURATION
import com.bzolyomi.shoppinglist.util.Constants.INTRO_SCREEN_EXIT_DURATION
import com.bzolyomi.shoppinglist.viewmodels.SharedViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationController(sharedViewModel: SharedViewModel) {

    val navController: NavHostController = rememberAnimatedNavController()

    val isInDarkMode = isSystemInDarkTheme()
    val backgroundModifier: Modifier =
        if (isInDarkMode) Modifier.background(Color.Black)
        else Modifier.background(GradientBackground)

    AnimatedNavHost(navController = navController, startDestination = "intro") {

        composable(
            route = "intro",
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(INTRO_SCREEN_EXIT_DURATION),
                    targetOffsetX = { -it }
                )
            }
        ) {
            IntroTheme {
                IntroScreen(
                    isInDarkMode = isInDarkMode,
                    onDelayPassed = {
                        navController.navigate("home") {
                            popUpTo("intro") { inclusive = true }
                        }
                    },
                    modifier = backgroundModifier.fillMaxSize()
                )
            }
        }

        composable("home") {
            val shoppingGroupsWithLists by sharedViewModel.shoppingGroupsWithLists.collectAsState()

            ShoppingListTheme {
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
        }

        composable(
            route = "add",
            enterTransition = {
                slideInHorizontally(
                    animationSpec = tween(
                        ADD_SCREEN_ENTER_DURATION,
                        0,
                        easing = LinearOutSlowInEasing
                    ),
                    initialOffsetX = { it / 2 }
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(ADD_SCREEN_EXIT_DURATION),
                    targetOffsetX = { it }
                )
            }
        ) {
            val isInputError = false

            ShoppingListTheme {
                AddAllScreen(
                    itemName = sharedViewModel.itemName,
                    itemQuantity = sharedViewModel.itemQuantity,
                    itemUnit = sharedViewModel.itemUnit,
                    onItemNameChange = { sharedViewModel.itemName = it },
                    onItemQuantityChange = { sharedViewModel.itemQuantity = it },
                    onItemUnitChange = { sharedViewModel.itemUnit = it },
                    onAddItemButtonClicked = {
                        if (!isInputError) {
                            val groupName by sharedViewModel.groupName
                            sharedViewModel.createWithCoroutines()
                            sharedViewModel.setGroupName(groupName)
                        }
                    },
                    onSubmitAddAllButtonClicked = {
                        if (!isInputError) {
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = true }
                            }
                            sharedViewModel.createWithCoroutines()
                        }
                    },
                    onNavigationBarBackButtonClicked = {
                        sharedViewModel.setGroupName("")
                        sharedViewModel.clearItemsList()
                        sharedViewModel.flushItemGUI()
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    sharedViewModel = sharedViewModel,
                    modifier = backgroundModifier
                        .fillMaxSize()
                )
            }
        }

        composable(
            route = "group/{groupId}",
            arguments = listOf(navArgument("groupId") {
                type = NavType.StringType
            }),
            enterTransition = {
                slideInHorizontally(
                    animationSpec = tween(
                        GROUP_SCREEN_ENTER_DURATION,
                        0,
                        easing = LinearOutSlowInEasing
                    ),
                    initialOffsetX = { it / 2 }
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(GROUP_SCREEN_EXIT_DURATION),
                    targetOffsetX = { it }
                )
            }
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

            ShoppingListTheme {
                ItemsOfGroupScreen(
                    selectedGroupWithList = selectedGroupWithList,
                    onDeleteItemClicked = {
                        sharedViewModel.deleteItem(itemId = it)
                    },
                    onDeleteGroupConfirmed = { groupIdToDelete, shoppingListToDelete ->
                        sharedViewModel.setCurrentGroupID(groupId = null)
                        sharedViewModel.deleteGroup(groupId = groupIdToDelete)
                        sharedViewModel.deleteItems(shoppingListToDelete)
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
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
                    onCancelAddItemButtonClicked = {
                        sharedViewModel.flushItemGUI()
                    },
                    onNavigationBarBackButtonClicked = {
                        sharedViewModel.flushItemGUI()
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    modifier = backgroundModifier
                        .fillMaxSize(),
                    sharedViewModel = sharedViewModel
//                onItemsRearrangedOnGUI = {
//                    sharedViewModel.rearrangeItems(selectedGroupWithList.shoppingList, it)
//                }
                )
            }
        }
    }
}
