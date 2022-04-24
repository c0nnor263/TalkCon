package com.conboi.talkcon.data.source.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.conboi.talkcon.data.dao.ChatDao
import com.conboi.talkcon.data.dao.UserChatDao
import com.conboi.talkcon.data.model.ChatMessage
import com.conboi.talkcon.data.model.UserChat

@Database(entities = [UserChat::class, ChatMessage::class], version = 1)
@TypeConverters(AppDatabaseConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getUserChatsDao(): UserChatDao
    abstract fun getChatDao(): ChatDao
}