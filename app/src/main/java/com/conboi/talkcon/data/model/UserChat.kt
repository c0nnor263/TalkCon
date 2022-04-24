package com.conboi.talkcon.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlin.random.Random

@Parcelize
@Entity(tableName = UserChat.TABLE_NAME)
data class UserChat(
    @PrimaryKey val id: String = "",
    val accountName: String = "Name:${Random.nextDouble()}",
    val avatarUrl: String = "2",
    val pinned: Boolean = false,
    val lastOnline: Long = System.currentTimeMillis(),
    val lastSendTimeMessage: Long = System.currentTimeMillis(),
    val lastMsg: ChatMessage? = null
) : Parcelable {
    companion object {
        const val ID = "id"
        const val ACCOUNT_NAME = "accountName"
        const val AVATAR_URL = "avatarUrl"
        const val PINNED = "pinned"
        const val LAST_ONLINE = "lastOnline"
        const val LAST_SEND_TIME_MESSAGE = "lastSendTimeMessage"
        const val LAST_MSG = "lastMsg"


        const val TABLE_NAME = "userChats"
    }

}
