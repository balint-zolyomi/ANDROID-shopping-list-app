package com.bzolyomi.shoppinglist.util

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
    const val SHOPPING_LIST_TABLE_COLUMN_ITEM_POSITION = "item_position"

    // NAVIGATION
    const val INTRO_SCREEN = "intro"
    const val HOME_SCREEN = "home"
    const val ADD_SCREEN = "add"
    const val ADD_SCREEN_WITH_ARG = "add/{groupId}"
    const val GROUP_UNSELECTED: Long = -1
    const val GROUP_SCREEN = "group"
    const val GROUP_SCREEN_WITH_ARG = "group/{groupId}"
    const val NAV_ARGUMENT_GROUP_ID = "groupId"
}