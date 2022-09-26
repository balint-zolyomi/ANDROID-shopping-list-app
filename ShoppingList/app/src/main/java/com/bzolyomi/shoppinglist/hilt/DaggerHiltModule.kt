package com.bzolyomi.shoppinglist.hilt

import android.content.Context
import androidx.room.Room
import com.bzolyomi.shoppinglist.data.LocalDatabase
import com.bzolyomi.shoppinglist.util.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaggerHiltModule {

    @Singleton
    @Provides
    fun provideLocalDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, LocalDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideLocalDatabaseDao(localDatabase: LocalDatabase) = localDatabase.localDatabaseDao()
}