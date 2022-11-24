package com.bzolyomi.shoppinglist.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.bzolyomi.shoppinglist.R
import com.bzolyomi.shoppinglist.ui.components.AppBarOptionMore
import com.bzolyomi.shoppinglist.ui.components.AppBarOptionToggleReorder
import com.bzolyomi.shoppinglist.ui.components.cards.ItemCards
import com.bzolyomi.shoppinglist.ui.components.cards.ItemCardsReorder
import com.bzolyomi.shoppinglist.ui.theme.FloatingActionButtonTint
import com.bzolyomi.shoppinglist.util.Constants.ITEM_CARDS_REORDER_TOGGLE_CROSSFADE_ANIMATION_DURATION
import com.bzolyomi.shoppinglist.SharedViewModel

@Composable
fun GroupScreen(
    navigateToHomeScreen: () -> Unit,
    navigateToAddItemScreen: (Long?) -> Unit,
    modifier: Modifier,
    sharedVM: SharedViewModel
) {

    var isReordering by remember { mutableStateOf(false) }

    BackHandler {
        if (isReordering) {
            isReordering = !isReordering
        } else {
            navigateToHomeScreen()
            sharedVM.flushItemGUI()
            sharedVM.flushGroupGUI()
        }
    }

    val context = LocalContext.current
    val reorderHint = stringResource(R.string.toast_message_reorder_hint)
    val toastMessageForGroupDelete = stringResource(R.string.toast_message_group_deleted)

    val shoppingGroup by mutableStateOf(sharedVM.selectedGroupWithList.group)
    val shoppingList by sharedVM.selectedShoppingList.collectAsState()

    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(text = shoppingGroup.groupName) },
                actions = {
                    AppBarOptionToggleReorder(
                        isReordering = isReordering,
                        onReorderButtonToggled = {
                            if (!isReordering) {
                                Toast.makeText(context, reorderHint, Toast.LENGTH_LONG).show()
                            }
                            isReordering = !isReordering
                        }
                    )
                    AppBarOptionMore(
                        dropdownItemTitle = stringResource(
                            R.string.appbar_dropdown_menu_option_delete_group
                        ),
                        alertDialogMessage = stringResource(
                            R.string.alert_dialog_message_delete_group
                        ),
                        onConfirmClicked = {
                            navigateToHomeScreen()
                            Toast.makeText(context, toastMessageForGroupDelete, Toast.LENGTH_SHORT)
                                .show()
                            sharedVM.deleteGroup(groupId = shoppingGroup.groupId)
                            sharedVM.deleteItems(shoppingList = shoppingList)
                        }
                    )
                },
                backgroundColor = MaterialTheme.colors.background
            )
        },
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        content = {
            Column(
                modifier = modifier
                    .fillMaxSize()
            ) {
                Crossfade(
                    targetState = isReordering,
                    animationSpec = tween(ITEM_CARDS_REORDER_TOGGLE_CROSSFADE_ANIMATION_DURATION)
                ) { isReordering ->
                    if (!isReordering) {
                        ItemCards(
                            shoppingList = shoppingList,
                            onCheckboxClicked = {
                                sharedVM.updateItemChecked(it)
                            },
                            onDeleteItemClicked = { itemId ->
                                sharedVM.deleteItem(
                                    itemId = itemId
                                )
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        ItemCardsReorder(
                            shoppingList = shoppingList,
                            onItemsOrderChange = {
                                sharedVM.updateShoppingListOrder(it)
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            if (!isReordering) {
                FloatingActionButton(
                    onClick = {
                        sharedVM.setGroupName(shoppingGroup.groupName)
                        navigateToAddItemScreen(shoppingGroup.groupId)
                    },
                    backgroundColor = MaterialTheme.colors.primary
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(R.string.content_description_fab_add_item),
                        tint = FloatingActionButtonTint,
                    )
                }
            }
        }
    )
}
