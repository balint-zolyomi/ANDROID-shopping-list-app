package com.bzolyomi.shoppinglist

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