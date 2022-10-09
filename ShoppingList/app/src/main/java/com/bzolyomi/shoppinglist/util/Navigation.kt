package com.bzolyomi.shoppinglist.util

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import com.google.accompanist.navigation.animation.composable
import androidx.navigation.navArgument
import com.bzolyomi.shoppinglist.R
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
import kotlinx.coroutines.launch

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

        composable(
            route = "home"
        ) {
//                navBackStackEntry ->
//            val isDeleteGroup = navBackStackEntry.arguments!!.getBoolean("isDeleteGroup")
//            if (isDeleteGroup) {
//                sharedViewModel.deleteGroupAndItsItems()

//                val context = LocalContext.current
//                val toastMessageForGroupDelete = stringResource(R.string.toast_message_group_deleted)
//                Toast.makeText(context, toastMessageForGroupDelete, Toast.LENGTH_SHORT).show()
//            }

            ShoppingListTheme {
                AllGroupsScreen(
                    onAddAllFABClicked = { navController.navigate("add") },
                    onOpenGroupIconClicked = { groupId ->
                        if (groupId != null) navController.navigate("group/$groupId")
                    },
                    sharedViewModel = sharedViewModel,
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
            ShoppingListTheme {
                AddAllScreen(
                    onSubmitAddAllButtonClicked = {
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    onNavigationBarBackButtonClicked = {
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

            if (groupId != null) {
                LaunchedEffect(key1 = true) {
                    sharedViewModel.selectedGroupWithList =
                        sharedViewModel.getSelectedGroupWithListCoroutine(groupId = groupId)
                }

                ShoppingListTheme {
                    val context = LocalContext.current
                    val toastMessageForGroupDelete = stringResource(R.string.toast_message_group_deleted)

                    ItemsOfGroupScreen(
                        groupWithList = sharedViewModel.selectedGroupWithList,
                        onDeleteGroupConfirmed = {
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = true }
                            }

                            Toast.makeText(context, toastMessageForGroupDelete, Toast.LENGTH_SHORT)
                                .show()
                        },
                        onNavigationBarBackButtonClicked = {
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = true }
                            }
                        },
                        modifier = backgroundModifier
                            .fillMaxSize(),
                        sharedViewModel = sharedViewModel
                        // onItemsRearrangedOnGUI = {
                        // sharedViewModel.rearrangeItems(selectedGroupWithList.shoppingList, it)
                        // }
                    )
                }
            }
        }
    }
}
