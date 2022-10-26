package com.bzolyomi.shoppinglist

import com.bzolyomi.shoppinglist.data.*
import com.bzolyomi.shoppinglist.hilt.DaggerHiltModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Singleton

@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DaggerHiltModule::class]
)
@Module
object FakeDaggerHiltModule {

    @Singleton
    @Provides
    fun provideFakeDao() = object : LocalDatabaseDao {

        private val group1 = ShoppingGroupEntity(
            groupId = 1,
            groupName = "Spar"
        )
        private val group2 = ShoppingGroupEntity(
            groupId = 2,
            groupName = "Lidl"
        )
        private val list1 = listOf(
            ShoppingItemEntity(
                1,
                1,
                "cheese",
                2F,
                "kg",
                false
            ),
            ShoppingItemEntity(
                2,
                1,
                "beer",
                5F,
                "l",
                false
            )
        )
        private val list2 = listOf(
            ShoppingItemEntity(
                3,
                2,
                "pasta",
                2F,
                "kg",
                false
            )
        )
        private val listOrder1 = listOf(
            ListOrderEntity(1, 1, 1, 1),
            ListOrderEntity(2, 1, 2, 2)
        )
        private val listOrder2 = listOf(
            ListOrderEntity(3, 2, 3, 1)
        )

        private val groupWithList1 = GroupWithList(
            group = group1,
            shoppingList = list1,
            listOrder = listOrder1
        )
        private val groupWithList2 = GroupWithList(
            group = group2,
            shoppingList = list2,
            listOrder = listOrder2
        )
        private val dummyItems = listOf(groupWithList1, groupWithList2)

        override suspend fun createGroup(group: ShoppingGroupEntity) {
            TODO("Not yet implemented")
        }

        override suspend fun createItem(item: ShoppingItemEntity) {
            TODO("Not yet implemented")
        }

        override suspend fun createListOrder(listOrder: ListOrderEntity) {
            TODO("Not yet implemented")
        }

        override fun getGroupsWithLists(): Flow<List<GroupWithList>> {
            return flowOf(dummyItems)
        }

        override suspend fun getGroupWithList(groupId: Long?): GroupWithList {
            TODO("Not yet implemented")
        }

        override fun getShoppingList(groupId: Long?): Flow<List<ShoppingItemEntity>> {
            TODO("Not yet implemented")
        }

        override fun getListOrder(groupId: Long?): Flow<List<ListOrderEntity>> {
            TODO("Not yet implemented")
        }

        override suspend fun getGroupId(groupName: String): Long? {
            TODO("Not yet implemented")
        }

        override suspend fun updateItem(item: ShoppingItemEntity) {
            TODO("Not yet implemented")
        }

        override suspend fun updateListOrder(listOrder: List<ListOrderEntity>) {
            TODO("Not yet implemented")
        }

        override suspend fun deleteGroup(groupId: Long?) {
            TODO("Not yet implemented")
        }

        override suspend fun deleteItem(itemId: Long?) {
            TODO("Not yet implemented")
        }

        override suspend fun deleteListOrder(groupId: Long?, itemId: Long?) {
            TODO("Not yet implemented")
        }

        override suspend fun deleteAllListOrders(groupId: Long?) {
            TODO("Not yet implemented")
        }
    }
}