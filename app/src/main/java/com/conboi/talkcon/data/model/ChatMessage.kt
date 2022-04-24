package com.conboi.talkcon.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.conboi.talkcon.utils.UserType
import kotlinx.parcelize.Parcelize

@Entity(tableName = ChatMessage.TABLE_NAME)
@Parcelize
data class ChatMessage(
    val chatId: String = "",
    @PrimaryKey val messageId: String = "",
    val textMessage: String = "",
    val sendTime: Long = System.currentTimeMillis(),
    val sendBy: String = "",
    var viewType: UserType? = null
) : Parcelable {
    companion object {
        const val CHAT_ID = "chatId"
        const val MESSAGE_ID = "messageId"
        const val TEXT_MESSAGE = "textMessage"
        const val SEND_TIME = "sendTime"
        const val SEND_BY = "sendBy"
        const val VIEW_TYPE = "viewType"

        const val TABLE_NAME = "chatMessages"
    }
}
