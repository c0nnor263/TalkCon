package com.conboi.talkcon.data.source.local.database

import androidx.room.TypeConverter
import com.conboi.talkcon.data.model.ChatMessage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class AppDatabaseConverters {

    @TypeConverter
    fun fromChatMessageToString(value: ChatMessage?): String? {
        if (value == null) {
            return null
        }
        val type = object : TypeToken<ChatMessage?>() {}.type
        return Gson().toJson(value, type)
    }

    @TypeConverter
    fun fromStringToChatMessage(value: String?): ChatMessage? {
        if (value == null) {
            return null
        }
        val type = object : TypeToken<ChatMessage?>() {}.type
        return Gson().fromJson(value, type)
    }
}