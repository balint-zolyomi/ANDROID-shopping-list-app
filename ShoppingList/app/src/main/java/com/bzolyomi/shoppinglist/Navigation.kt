package com.bzolyomi.shoppinglist

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bzolyomi.shoppinglist.ui.screens.AddScreen
import com.bzolyomi.shoppinglist.ui.screens.AllGroupsScreen
import com.bzolyomi.shoppinglist.ui.screens.GroupScreen
import com.bzolyomi.shoppinglist.ui.screens.IntroScreen
import com.bzolyomi.shoppinglist.ui.theme.GradientBackground
import com.bzolyomi.shoppinglist.ui.theme.IntroTheme
import com.bzolyomi.shoppinglist.ui.theme.ShoppingListTheme
import com.bzolyomi.shoppinglist.util.Constants.ADD_SCREEN
import com.bzolyomi.shoppinglist.util.Constants.ADD_SCREEN_WITH_ARG
import com.bzolyomi.shoppinglist.util.Constants.GROUP_SCREEN
import com.bzolyomi.shoppinglist.util.Constants.GROUP_SCREEN_WITH_ARG
import com.bzolyomi.shoppinglist.util.Constants.GROUP_UNSELECTED
import com.bzolyomi.shoppinglist.util.Constants.HOME_SCREEN
import com.bzolyomi.shoppinglist.util.Constants.INTRO_SCREEN
import com.bzolyomi.shoppinglist.util.Constants.NAV_ARGUMENT_GROUP_ID

@Composable
fun NavigationController(sharedViewModel: SharedViewModel) {

    val navController: NavHostController = rememberNavController()

    val isInDarkMode = isSystemInDarkTheme()
    val backgroundModifier: Modifier =
        if (isInDarkMode) Modifier.background(Color.Black)
        else Modifier.background(GradientBackground)

    NavHost(navController = navController, startDestination = INTRO_SCREEN) {

        composable(
            INTRO_SCREEN
//            route = INTRO_SCREEN,
//            exitTransition = {
//                slideOutHorizontally(
//                    animationSpec = tween(INTRO_SCREEN_EXIT_DURATION)
//                )
//            }
        ) {
            IntroTheme {
                IntroScreen(
                    isInDarkMode = isInDarkMode,
                    onAnimationEnded = {
                        navController.navigate(HOME_SCREEN) {
                            popUpTo(INTRO_SCREEN) { inclusive = true }
                        }
                    },
                    modifier = backgroundModifier.fillMaxSize()
                )
            }
        }

        composable(
            route = HOME_SCREEN
        ) {
            ShoppingListTheme {
                AllGroupsScreen(
                    navigateToAddScreen = {
                        navController.navigate("$ADD_SCREEN/$GROUP_UNSELECTED")
                    },
                    navigateToGroupScreen = { groupId ->
                        if (groupId != null) {
                            navController.navigate("$GROUP_SCREEN/$groupId") {
                                popUpTo("$GROUP_SCREEN/$groupId") {
                                    inclusive = true
                                    saveState = false
                                }
                            }
                        }
                    },
                    sharedVM = sharedViewModel,
                    modifier = backgroundModifier
                        .fillMaxSize()
                )
            }
        }

        composable(
            route = ADD_SCREEN_WITH_ARG,
            arguments = listOf(navArgument(NAV_ARGUMENT_GROUP_ID) {
                type = NavType.StringType
            }),
//            enterTransition = {
//                slideInHorizontally(
//                    animationSpec = tween(ADD_SCREEN_ENTER_DURATION),
//                    initialOffsetX = { it / 2 }
//                )
//            },
//            exitTransition = {
//                slideOutHorizontally(
//                    animationSpec = tween(ADD_SCREEN_EXIT_DURATION),
//                    targetOffsetX = { it / 2 }
//                )
//            }
        ) { navBackStackEntry ->
            val groupId = navBackStackEntry.arguments?.getString(NAV_ARGUMENT_GROUP_ID)?.toLong()

            ShoppingListTheme {
                AddScreen(
                    groupId = groupId,
                    navigateToHomeScreen = {
                        navController.navigate(HOME_SCREEN) {
                            popUpTo(HOME_SCREEN) { inclusive = true }
                        }
                    },
                    navigateToGroupScreen = {
                        navController.navigate("$GROUP_SCREEN/$groupId") {
                            popUpTo("$GROUP_SCREEN/$groupId") { inclusive = true }
                        }
                    },
                    sharedViewModel = sharedViewModel,
                    modifier = backgroundModifier
                        .fillMaxSize()
                )
            }
        }

        composable(
            route = GROUP_SCREEN_WITH_ARG,
            arguments = listOf(navArgument(NAV_ARGUMENT_GROUP_ID) {
                type = NavType.StringType
            }),
//            enterTransition = {
//                when (initialState.destination.route) {
//                    HOME_SCREEN -> slideInHorizontally(
//                        animationSpec = tween(GROUP_SCREEN_ENTER_DURATION),
//                        initialOffsetX = { it / 2 }
//                    )
//                    else -> null
//                }
//            },
//            exitTransition = {
//                when (targetState.destination.route) {
//                    HOME_SCREEN -> slideOutHorizontally(
//                        animationSpec = tween(GROUP_SCREEN_EXIT_DURATION),
//                        targetOffsetX = { it / 2 }
//                    )
//                    else -> null
//                }
//            }
        ) { navBackStackEntry ->
            val groupId = navBackStackEntry.arguments!!.getString(NAV_ARGUMENT_GROUP_ID)

            Log.d("balint-debug", groupId.toString())
            if (groupId != null) {
                ShoppingListTheme {
                    GroupScreen(
                        navigateToHomeScreen = {
                            navController.navigate(HOME_SCREEN) {
                                popUpTo(HOME_SCREEN) {
                                    inclusive = true
                                    saveState = false
                                }
                            }
                        },
                        navigateToAddItemScreen = { groupId ->
                            navController.navigate("$ADD_SCREEN/$groupId")
                        },
                        modifier = backgroundModifier
                            .fillMaxSize(),
                        sharedVM = sharedViewModel
                    )
                }
            }
        }
    }
}