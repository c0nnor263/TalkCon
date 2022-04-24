package com.conboi.talkcon.di.module

import android.content.Context
import androidx.room.Room
import com.conboi.talkcon.data.source.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideUserChatDao(db: AppDatabase) = db.getUserChatsDao()

    @Singleton
    @Provides
    fun provideChatDao(db:AppDatabase) = db.getChatDao()

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext app: Context): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java, "appDatabase").build()
    }
}