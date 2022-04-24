package com.conboi.talkcon.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.conboi.talkcon.data.model.ChatMessage

@Dao
interface ChatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(chatMessages: List<ChatMessage>)

    @Query("SELECT * FROM chatMessages WHERE chatId LIKE '%' || :chatId || '%'")
    fun pagingSource(chatId: String): PagingSource<Int, ChatMessage>

    @Query("DELETE FROM chatMessages")
    suspend fun clearAll()
}