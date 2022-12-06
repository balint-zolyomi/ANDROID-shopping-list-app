package com.bzolyomi.shoppinglist.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import com.bzolyomi.shoppinglist.R
import com.bzolyomi.shoppinglist.SharedViewModel
import com.bzolyomi.shoppinglist.ui.components.input.*
import com.bzolyomi.shoppinglist.ui.components.showShortToast
import com.bzolyomi.shoppinglist.ui.theme.FloatingActionButtonTint
import com.bzolyomi.shoppinglist.util.Constants.GROUP_NAME_MAX_LENGTH
import com.bzolyomi.shoppinglist.util.Constants.GROUP_UNSELECTED
import com.bzolyomi.shoppinglist.util.Constants.ITEM_NAME_MAX_LENGTH
import com.bzolyomi.shoppinglist.util.Constants.ITEM_QUANTITY_MAX_LENGTH
import com.bzolyomi.shoppinglist.util.Constants.ITEM_UNIT_MAX_LENGTH
import com.bzolyomi.shoppinglist.util.Constants.PADDING_MEDIUM

@Composable
fun AddScreen(
    groupId: Long?,
    navigateToHomeScreen: () -> Unit,
    navigateToGroupScreen: () -> Unit,
    sharedViewModel: SharedViewModel,
    modifier: Modifier
) {
    BackHandler {
        if (groupId == GROUP_UNSELECTED) {
            navigateToHomeScreen()
            sharedViewModel.flushGroupGUI()
            sharedViewModel.clearItemsList()
        } else {
            navigateToGroupScreen()
        }
        sharedViewModel.flushItemGUI()
    }

    val inputTextStyle = if (isSystemInDarkTheme()) {
        LocalTextStyle.current.copy(color = Color.White)
    } else {
        LocalTextStyle.current
    }

    val context = LocalContext.current
    val itemAddedToastMessage = stringResource(R.string.toast_message_item_added)

    val groupName by sharedViewModel.groupName
    val itemName by sharedViewModel.itemName
    val itemQuantity by sharedViewModel.itemQuantity
    val itemUnit by sharedViewModel.itemUnit

    var isGroupNameError by rememberSaveable { mutableStateOf(false) }
    var isItemNameError by rememberSaveable { mutableStateOf(false) }
    var isItemQuantityError by rememberSaveable { mutableStateOf(false) }
    var isItemUnitError by rememberSaveable { mutableStateOf(false) }

    var isAnyError by rememberSaveable { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val focusRequester = FocusRequester()

    LaunchedEffect(true) {
        focusRequester.requestFocus()
    }

    fun validateAll() {
        isGroupNameError = validateGroupNameInput(groupName)
        isItemNameError = validateItemNameInput(itemName)
        isItemQuantityError = validateItemQuantityInput(itemQuantity)
        isItemUnitError = validateItemUnitInput(itemUnit)
        isAnyError = isGroupNameError || isItemNameError || isItemQuantityError || isItemUnitError
    }

    fun onSubmit() {
        validateAll()
        if (!isAnyError) {
            focusManager.clearFocus()
            if (groupId == GROUP_UNSELECTED) navigateToHomeScreen() else navigateToGroupScreen()
            sharedViewModel.createWithCoroutines()
            showShortToast(context = context, message = itemAddedToastMessage)
        }
    }

    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding(),
        topBar = {
            TopAppBar(
                title = {
                    if (groupId == GROUP_UNSELECTED) {
                        Text(text = stringResource(R.string.appbar_title_add_group_and_items))
                    } else {
                        Text(
                            text = stringResource(
                                R.string.appbar_title_add_item
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
                GroupNameInput(
                    groupName = groupName,
                    isError = isGroupNameError,
                    isEnabled = groupId == GROUP_UNSELECTED,
                    inputTextStyle = inputTextStyle,
                    onGroupNameChange = {
                        isGroupNameError = validateGroupNameInput(it)
                        // it == "" case means that user wants to delete the input field with TrailingIcon
                        if (!isGroupNameError || it == "" || it.length == GROUP_NAME_MAX_LENGTH) {
                            sharedViewModel.setGroupName(it)
                        }
                    },
                    onEraseGroupNameInputButtonClicked = {
                        sharedViewModel.setGroupName("")
                        isGroupNameError = validateGroupNameInput(sharedViewModel.groupName.value)
                    },
                    onNextInGroupNameInputClicked = {
                        isGroupNameError = validateGroupNameInput(groupName)
                    },
                    modifier = if (groupId == GROUP_UNSELECTED) {
                        Modifier.focusRequester(focusRequester)
//                        Modifier
                    } else {
                        Modifier
                    }
                )
                ItemNameInput(
                    itemName = itemName,
                    isError = isItemNameError,
                    inputTextStyle = inputTextStyle,
                    onItemNameChange = {
                        isItemNameError = validateItemNameInput(it)
                        if (!isItemNameError || it == "" || it.length == ITEM_NAME_MAX_LENGTH) {
                            sharedViewModel.setItemName(it)
                        }
                    },
                    onEraseItemNameInputButtonClicked = {
                        sharedViewModel.setItemName("")
                        isItemNameError = validateItemNameInput(sharedViewModel.itemName.value)
                    },
                    onNextInItemNameInputClicked = {
                        isItemNameError = validateItemNameInput(itemName)
                    },
                    modifier = if (groupId != GROUP_UNSELECTED) {
                        Modifier.focusRequester(focusRequester)
//                        Modifier
                    } else {
                        Modifier
                    }
                )
                ItemQuantityInput(
                    itemQuantity = itemQuantity,
                    isError = isItemQuantityError,
                    inputTextStyle = inputTextStyle,
                    onItemQuantityChange = {
                        isItemQuantityError = validateItemQuantityInput(it)
                        if (!isItemQuantityError || it == ""
                            || it.length == ITEM_QUANTITY_MAX_LENGTH
                        ) {
                            sharedViewModel.setItemQuantity(it)
                        }
                    },
                    onEraseItemQuantityInputButtonClicked = {
                        sharedViewModel.setItemQuantity("")
                        isItemQuantityError = validateItemQuantityInput(
                            sharedViewModel.itemQuantity.value
                        )
                    },
                    onNextInItemQuantityInputClicked = {
                        isItemQuantityError = validateItemQuantityInput(itemQuantity)
                    }
                )
                ItemUnitInput(
                    itemUnit = itemUnit,
                    isError = isItemUnitError,
                    inputTextStyle = inputTextStyle,
                    onItemUnitChange = {
                        isItemUnitError = validateItemUnitInput(it)
                        if (!isItemUnitError || it == "" || it.length == ITEM_UNIT_MAX_LENGTH) {
                            sharedViewModel.setItemUnit(it)
                        }
                    },
                    onEraseItemUnitInputButtonClicked = {
                        sharedViewModel.setItemUnit("")
                        isItemUnitError = validateItemUnitInput(sharedViewModel.itemUnit.value)
                    },
                    onDone = { onSubmit() }
                )
                if (groupId == GROUP_UNSELECTED) {
                    AddItemButton(onAddItemButtonClicked = {
                        validateAll()
                        if (!isAnyError) {
                            val tempGroupName = groupName
                            sharedViewModel.createWithCoroutines()
                            sharedViewModel.setGroupName(tempGroupName)
                            showShortToast(context = context, message = itemAddedToastMessage)
                            focusManager.clearFocus()
                            focusManager.moveFocus(FocusDirection.Down)
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    })
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
                    contentDescription = stringResource(
                        R.string.content_description_fab_confirm_add
                    ),
                    tint = FloatingActionButtonTint
                )
            }
        }
    )
}

@Composable
fun AddItemButton(onAddItemButtonClicked: () -> Unit) {
    Button(
        onClick = onAddItemButtonClicked,
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
        modifier = Modifier.padding(PADDING_MEDIUM)
    ) {
        Text(text = stringResource(R.string.button_add_item_text))
    }
}
