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
}