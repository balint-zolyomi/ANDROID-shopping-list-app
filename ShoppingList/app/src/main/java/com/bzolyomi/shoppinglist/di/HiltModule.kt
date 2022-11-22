package com.bzolyomi.shoppinglist.di

import android.content.Context
import androidx.room.Room
import com.bzolyomi.shoppinglist.data.Database
import com.bzolyomi.shoppinglist.util.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, Database::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideDao(database: Database) = database.DAO()
}