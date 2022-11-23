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

        val groupWithList1 = GroupWithList(group1, listOf(item1, item2))
        val groupWithList2 = GroupWithList(group2, listOf(item3))

        return listOf(groupWithList1, groupWithList2)
    }
}