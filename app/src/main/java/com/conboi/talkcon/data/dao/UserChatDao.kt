package com.conboi.talkcon.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.conboi.talkcon.data.model.UserChat

@Dao
interface UserChatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(userChats: List<UserChat>)

    @Query("SELECT * FROM userChats ORDER BY lastSendTimeMessage DESC")
    fun pagingSource(): PagingSource<Int, UserChat>

    @Query("DELETE FROM userChats")
    suspend fun clearAll()
}