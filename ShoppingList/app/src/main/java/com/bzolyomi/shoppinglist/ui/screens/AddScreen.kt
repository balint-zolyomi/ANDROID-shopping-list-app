package com.bzolyomi.shoppinglist.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.bzolyomi.shoppinglist.R
import com.bzolyomi.shoppinglist.ui.components.GroupInput
import com.bzolyomi.shoppinglist.ui.components.ItemInput
import com.bzolyomi.shoppinglist.ui.components.showItemAddedToast
import com.bzolyomi.shoppinglist.ui.theme.FloatingActionButtonTint
import com.bzolyomi.shoppinglist.util.Constants.PADDING_MEDIUM
import com.bzolyomi.shoppinglist.viewmodels.SharedViewModel

@Composable
fun AddAllScreen(
    groupId: Long?,
    NavigateToHomeScreen: () -> Unit,
    NavigateToItemsOfGroupScreen: () -> Unit,
    sharedViewModel: SharedViewModel,
    modifier: Modifier
) {
    BackHandler {
        if (groupId == -1L) {
            NavigateToHomeScreen()
            sharedViewModel.setGroupName("")
            sharedViewModel.clearItemsList()
        } else {
            NavigateToItemsOfGroupScreen()
        }
        sharedViewModel.flushItemGUI()
    }

    val context = LocalContext.current

    val groupName by sharedViewModel.groupName
    val itemName by sharedViewModel.itemName
    val itemQuantity by sharedViewModel.itemQuantity
    val itemUnit by sharedViewModel.itemUnit

    var isGroupNameError by rememberSaveable { mutableStateOf(false) }
    var isItemNameError by rememberSaveable { mutableStateOf(false) }
    var isItemQuantityError by rememberSaveable { mutableStateOf(false) }

    fun validateGroupNameInput(groupNameInput: String) {
        isGroupNameError = groupNameInput.isBlank()
    }

    fun validateItemNameInput(itemNameInput: String) {
        isItemNameError = itemNameInput.isBlank()
    }

    fun validateItemQuantityInput(itemQuantityInput: String) {
        isItemQuantityError = try {
            val tempQuantity = itemQuantityInput.replace(",", ".")
            tempQuantity.toFloat()
            false
        } catch (e: Exception) {
            itemQuantityInput != ""
        }
    }

    fun onSubmit() {
        validateGroupNameInput(groupName)
        validateItemNameInput(itemName)
        validateItemQuantityInput(itemQuantity)
        if (!isItemNameError && !isGroupNameError && !isItemQuantityError) {
            if (groupId == -1L) NavigateToHomeScreen() else NavigateToItemsOfGroupScreen()
            sharedViewModel.createWithCoroutines()
            showItemAddedToast(context)
        }
    }

    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    if (groupId == -1L) {
                        Text(text = stringResource(R.string.top_appbar_title_add_group_and_items))
                    } else {
                        Text(
                            text = stringResource(
                                R.string.top_appbar_title_add_items
                            )
                        )
                    }
                },
                backgroundColor = MaterialTheme.colors.background
            )
        },
        content = {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(PADDING_MEDIUM)
            ) {
                GroupInput(
                    groupName = groupName,
                    groupId = groupId,
                    isError = isGroupNameError,
                    onGroupNameChange = {
                        validateGroupNameInput(it)
                        if (!isGroupNameError || it == "") sharedViewModel.setGroupName(it)
                    },
                    onEraseGroupNameInputButtonClicked = {
                        sharedViewModel.setGroupName("")
                        validateGroupNameInput(sharedViewModel.groupName.value)
                    },
                    onNextInGroupNameInputClicked = { validateGroupNameInput(groupName) }
                )
                ItemInput(
                    itemName = itemName,
                    isItemNameError = isItemNameError,
                    onItemNameChange = {
                        validateItemNameInput(it)
                        if (!isItemNameError || it == "") sharedViewModel.setItemName(it)
                    },
                    onEraseItemNameInputButtonClicked = {
                        sharedViewModel.setItemName("")
                        validateItemNameInput(sharedViewModel.itemName.value)
                    },
                    onNextInItemNameInputClicked = { validateItemNameInput(itemName) },
                    itemQuantity = itemQuantity,
                    isItemQuantityError = isItemQuantityError,
                    onItemQuantityChange = { sharedViewModel.setItemQuantity(it) },
                    onEraseItemQuantityInputButtonClicked = {
                        sharedViewModel.setItemQuantity("")
                        validateItemQuantityInput(sharedViewModel.itemQuantity.value)
                    },
                    onNextInItemQuantityInputClicked = { validateItemQuantityInput(itemQuantity) },
                    itemUnit = itemUnit,
                    onItemUnitChange = { sharedViewModel.setItemUnit(it) },
                    onEraseItemUnitInputButtonClicked = {
                        sharedViewModel.setItemUnit("")
                    },
                    onDone = { onSubmit() }
                )
                if (groupId == -1L) {
                    Row(modifier = Modifier.padding(PADDING_MEDIUM)) {
                        AddItemButton(onAddItemButtonClicked = {
                            validateGroupNameInput(groupName)
                            validateItemNameInput(itemName)
                            validateItemQuantityInput(itemQuantity)
                            if (!isItemNameError && !isGroupNameError && !isItemQuantityError) {
                                val tempGroupName = groupName
                                sharedViewModel.createWithCoroutines()
                                sharedViewModel.setGroupName(tempGroupName)
                                showItemAddedToast(context)
                            }
                        })
                        Spacer(Modifier.weight(1f))
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onSubmit() },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = stringResource(R.string.content_description_fab),
                    tint = FloatingActionButtonTint,
                )
            }
        }
    )
}

@Composable
fun AddItemButton(onAddItemButtonClicked: () -> Unit) {
    Button(
        onClick = onAddItemButtonClicked,
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
    ) {
        Text(text = stringResource(R.string.add_item_button_text))
    }
}
