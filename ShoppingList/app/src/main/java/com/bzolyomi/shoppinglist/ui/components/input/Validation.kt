package com.bzolyomi.shoppinglist.ui.components.input

import com.bzolyomi.shoppinglist.util.Constants.GROUP_NAME_MAX_LENGTH
import com.bzolyomi.shoppinglist.util.Constants.ITEM_NAME_MAX_LENGTH
import com.bzolyomi.shoppinglist.util.Constants.ITEM_QUANTITY_MAX_LENGTH
import com.bzolyomi.shoppinglist.util.Constants.ITEM_UNIT_MAX_LENGTH

fun validateGroupNameInput(groupNameInput: String): Boolean {
    return groupNameInput.isBlank() || groupNameInput.length >= GROUP_NAME_MAX_LENGTH
}

fun validateItemNameInput(itemNameInput: String): Boolean {
    return itemNameInput.isBlank() || itemNameInput.length >= ITEM_NAME_MAX_LENGTH
}

fun validateItemQuantityInput(itemQuantityInput: String): Boolean {
    return if (itemQuantityInput.length >= ITEM_QUANTITY_MAX_LENGTH) {
        true
    } else {
        try {
            val tempQuantity = itemQuantityInput.replace(",", ".")
            tempQuantity.toFloat()
            false
        } catch (e: Exception) {
            itemQuantityInput != ""
        }
    }
}

fun validateItemUnitInput(itemUnitInput: String): Boolean {
    return itemUnitInput.length >= ITEM_UNIT_MAX_LENGTH
}