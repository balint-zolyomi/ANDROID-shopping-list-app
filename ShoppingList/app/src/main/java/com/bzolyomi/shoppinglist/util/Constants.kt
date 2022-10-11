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

    const val LIST_ORDER_TABLE = "list_order"
    const val LIST_ORDER_TABLE_COLUMN_LIST_ORDER_ID = "list_order_id"
    const val LIST_ORDER_TABLE_COLUMN_GROUP_ID = "group_id"
    const val LIST_ORDER_TABLE_COLUMN_ITEM_ID = "item_id"
    const val LIST_ORDER_TABLE_COLUMN_ITEM_POSITION = "item_position"

    // STYLE
        // Padding
    val PADDING_ZERO = 0.dp
    val PADDING_X_SMALL = 4.dp
    val PADDING_SMALL = 8.dp
    val PADDING_MEDIUM = 12.dp
    val PADDING_LARGE = 24.dp
    val PADDING_X_LARGE = 36.dp
    val PADDING_XX_LARGE = 72.dp

        // Elevation
    val ELEVATION_SMALL = 2.dp
    val ELEVATION_MEDIUM = 8.dp

        // Size
    val SIZE_MEDIUM = 25.dp
    val SIZE_ICONS_OFFICIAL = 48.dp

    // ANIMATION
        // Group card
    const val GROUP_CARD_FADE_IN_INITIAL_ALPHA = 0.0f
    const val GROUP_CARD_FADE_IN_DURATION = 200
    const val GROUP_CARD_FADE_OUT_DURATION = 100

        // Expand icon
    const val EXPAND_ICON_ROTATION_ANIMATION_START_DEGREES = 0f
    const val EXPAND_ICON_ROTATION_ANIMATION_END_DEGREES = -180f

        // Intro screen
    const val INTRO_SCREEN_INITIAL_DELAY: Long = 0
    const val INTRO_SCREEN_ZOOM_ANIMATION_DURATION: Int = 400
    const val INTRO_SCREEN_BETWEEN_ANIMATIONS_DELAY: Long = 700
    const val INTRO_SCREEN_FINISH_DELAY: Long = 1000

    val INTRO_SCREEN_ZOOM_ANIMATION_FINISH_SIZE = 800.dp
    val INTRO_SCREEN_ZOOM_ANIMATION_START_SIZE = 0.dp
    val INTRO_SCREEN_SPRING_ANIMATION_FINISH_POSITION = (-50).dp
    val INTRO_SCREEN_SPRING_ANIMATION_START_POSITION = (-200).dp

        // Transition
    const val INTRO_SCREEN_EXIT_DURATION = 800
    const val GROUP_SCREEN_ENTER_DURATION = 800
    const val GROUP_SCREEN_EXIT_DURATION = 800
    const val ADD_SCREEN_ENTER_DURATION = 800
    const val ADD_SCREEN_EXIT_DURATION = 800
}