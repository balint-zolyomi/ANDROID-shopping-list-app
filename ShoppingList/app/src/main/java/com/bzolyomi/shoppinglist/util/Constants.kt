package com.bzolyomi.shoppinglist.util

import androidx.compose.ui.unit.dp

object Constants {

    // DATABASE
    const val DATABASE_NAME = "shopping_list_database"

    const val SHOPPING_GROUP_TABLE = "shopping_group"
    const val SHOPPING_GROUP_TABLE_COLUMN_GROUP_ID = "group_id"
    const val SHOPPING_GROUP_TABLE_COLUMN_GROUP_NAME = "group_name"

    const val SHOPPING_LIST_TABLE = "shopping_list"
    const val SHOPPING_LIST_TABLE_COLUMN_ITEM_ID = "item_id"
    const val SHOPPING_LIST_TABLE_COLUMN_GROUP_ID = "parent_id"
    const val SHOPPING_LIST_TABLE_COLUMN_ITEM_NAME = "item_name"
    const val SHOPPING_LIST_TABLE_COLUMN_ITEM_QUANTITY = "item_quantity"
    const val SHOPPING_LIST_TABLE_COLUMN_ITEM_UNIT = "item_unit"
    const val SHOPPING_LIST_TABLE_COLUMN_ITEM_CHECKED = "item_checked"

    // STYLE
        // Padding
    val PADDING_ZERO = 0.dp
    val PADDING_X_SMALL = 4.dp
    val PADDING_SMALL = 8.dp
    val PADDING_MEDIUM = 12.dp
    val PADDING_LARGE = 24.dp
    val PADDING_X_LARGE = 36.dp

        // Elevation
    val ELEVATION_SMALL = 2.dp
    val ELEVATION_MEDIUM = 8.dp

        // Size
    val SIZE_MEDIUM = 25.dp

        // Animation
    const val GROUP_CARD_FADE_IN_INITIAL_ALPHA = 0.0f
    const val GROUP_CARD_FADE_IN_DURATION = 200
    const val GROUP_CARD_FADE_OUT_DURATION = 100
}