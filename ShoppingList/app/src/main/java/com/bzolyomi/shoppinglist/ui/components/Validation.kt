package com.bzolyomi.shoppinglist.ui.components

fun validateGroupNameInput(groupNameInput: String): Boolean {
    return groupNameInput.isBlank()
}

fun validateItemNameInput(itemNameInput: String): Boolean {
    return itemNameInput.isBlank()
}

fun validateItemQuantityInput(itemQuantityInput: String): Boolean {
    return try {
        val tempQuantity = itemQuantityInput.replace(",", ".")
        tempQuantity.toFloat()
        false
    } catch (e: Exception) {
        itemQuantityInput != ""
    }
}