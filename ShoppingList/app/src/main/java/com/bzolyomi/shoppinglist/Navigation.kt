package com.bzolyomi.shoppinglist

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.bzolyomi.shoppinglist.ui.screens.AddScreen
import com.bzolyomi.shoppinglist.ui.screens.AllGroupsScreen
import com.bzolyomi.shoppinglist.ui.screens.GroupScreen
import com.bzolyomi.shoppinglist.util.Constants.ADD_SCREEN
import com.bzolyomi.shoppinglist.util.Constants.ADD_SCREEN_WITH_ARG
import com.bzolyomi.shoppinglist.util.Constants.GROUP_SCREEN
import com.bzolyomi.shoppinglist.util.Constants.GROUP_SCREEN_WITH_ARG
import com.bzolyomi.shoppinglist.util.Constants.GROUP_UNSELECTED
import com.bzolyomi.shoppinglist.util.Constants.HOME_SCREEN
import com.bzolyomi.shoppinglist.util.Constants.NAV_ARGUMENT_GROUP_ID
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationController(sharedViewModel: SharedViewModel) {

    val navController: NavHostController = rememberAnimatedNavController()

    AnimatedNavHost(navController = navController, startDestination = HOME_SCREEN) {

        composable(
            route = HOME_SCREEN
        ) {
            AllGroupsScreen(
                navigateToAddScreen = {
                    navController.navigate("$ADD_SCREEN/$GROUP_UNSELECTED")
                },
                navigateToGroupScreen = { groupId ->
                    if (groupId != null) navController.navigate("$GROUP_SCREEN/$groupId")
                },
                sharedVM = sharedViewModel
            )
        }

        composable(
            route = ADD_SCREEN_WITH_ARG,
            arguments = listOf(navArgument(NAV_ARGUMENT_GROUP_ID) {
                type = NavType.StringType
            }),
        ) {
            AddScreen()
        }

        composable(
            route = GROUP_SCREEN_WITH_ARG,
            arguments = listOf(navArgument(NAV_ARGUMENT_GROUP_ID) {
                type = NavType.StringType
            })
        ) { navBackStackEntry ->
            val groupId = navBackStackEntry.arguments!!.getString(NAV_ARGUMENT_GROUP_ID)

            if (groupId != null) {
                LaunchedEffect(key1 = true) {
                    sharedViewModel.selectedGroupWithList =
                        sharedViewModel.getSelectedGroupWithListCoroutine(groupId = groupId)
                }

                GroupScreen(
                    groupWithList = sharedViewModel.selectedGroupWithList
                )
            }
        }
    }
}