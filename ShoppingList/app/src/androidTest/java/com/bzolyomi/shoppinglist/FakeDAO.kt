package com.bzolyomi.shoppinglist

import com.bzolyomi.shoppinglist.data.*
import com.bzolyomi.shoppinglist.di.HiltModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Singleton

//@TestInstallIn(
//    components = [SingletonComponent::class],
//    replaces = [HiltModule::class]
//)
//@Module
//object FakeDaggerHiltModule {
//
//    @Singleton
//    @Provides
//    fun provideFakeDAO() = object : DAO {
//
//        override suspend fun createGroup(group: ShoppingGroupEntity) {
//            TODO("Not yet implemented")
//        }
//
//        override suspend fun createItem(item: ShoppingItemEntity) {
//            TODO("Not yet implemented")
//        }
//
//        override fun getAll(): Flow<List<GroupWithList>> {
//            return flowOf(DummyData.groupsWithList)
//        }
//
//    }
//}