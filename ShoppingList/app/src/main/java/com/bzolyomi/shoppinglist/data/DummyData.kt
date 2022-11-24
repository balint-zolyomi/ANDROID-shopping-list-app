package com.bzolyomi.shoppinglist.data

object DummyData {
    val groupsWithList = createDummyData()

    private fun createDummyData(): List<GroupWithList> {
        val group1 = ShoppingGroupEntity(
            groupId = 1,
            groupName = "Spar"
        )
        val group2 = ShoppingGroupEntity(
            groupId = 2,
            groupName = "Lidl"
        )
        val item1 =
            ShoppingItemEntity(
                1,
                1,
                "cheese",
                2F,
                "kg",
                false,
                1
            )
        val item2 =
            ShoppingItemEntity(
                2,
                1,
                "beer",
                5F,
                "l",
                false,
                2
            )
        val item3 =
            ShoppingItemEntity(
                3,
                2,
                "pasta",
                2F,
                "kg",
                false,
                1
            )
        val item4 =
            ShoppingItemEntity(
                4,
                2,
                "chocolate",
                2F,
                "packs",
                false,
                2
            )
        val item5 =
            ShoppingItemEntity(
                5,
                2,
                "humidifier",
                null,
                "",
                false,
                3
            )
        val item6 =
            ShoppingItemEntity(
                6,
                2,
                "eggs",
                20F,
                "",
                false,
                4
            )
        val item7 =
            ShoppingItemEntity(
                7,
                2,
                "broccoli",
                1.5F,
                "pieces",
                false,
                5
            )
        val item8 =
            ShoppingItemEntity(
                8,
                2,
                "frozen pizza",
                3F,
                "",
                false,
                6
            )
        val item9 =
            ShoppingItemEntity(
                9,
                2,
                "tomatoes",
                1F,
                "kg",
                false,
                7
            )
        val item10 =
            ShoppingItemEntity(
                10,
                2,
                "cabbage",
                1F,
                "piece",
                false,
                8
            )

        val groupWithList1 = GroupWithList(group1, listOf(item1, item2))
        val groupWithList2 =
            GroupWithList(group2, listOf(item3
//                , item4, item5, item6, item7, item8, item9, item10
            ))

        return listOf(groupWithList1, groupWithList2)
    }
}