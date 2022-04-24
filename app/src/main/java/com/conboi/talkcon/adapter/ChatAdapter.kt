package com.conboi.talkcon.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.conboi.talkcon.data.model.ChatMessage
import com.conboi.talkcon.databinding.ItemCurrentUserMessageBinding
import com.conboi.talkcon.databinding.ItemOtherUserMessageBinding

class ChatAdapter :
    PagingDataAdapter<ChatMessage, MessageViewHolder>(DiffUtilCallback()) {

    class CurrentUserMessageViewHolder(private val binding: ItemCurrentUserMessageBinding) :
        MessageViewHolder(binding) {
        override fun onBind(chat: ChatMessage) {
            binding.tvMessage.text = chat.textMessage
        }
    }

    class OtherUserMessageViewHolder(private val binding: ItemOtherUserMessageBinding) :
        MessageViewHolder(binding) {
        override fun onBind(chat: ChatMessage) {
            binding.tvMessage.text = chat.textMessage
        }
    }


    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val item = getItem(position) ?: return
        when (holder) {
            is CurrentUserMessageViewHolder -> holder.onBind(item)
            is OtherUserMessageViewHolder -> holder.onBind(item)
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MessageViewHolder {
        return when (viewType) {
            0 -> {
                CurrentUserMessageViewHolder(
                    ItemCurrentUserMessageBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ),
                        parent,
                        false
                    )
                )
            }
            1 -> {
                OtherUserMessageViewHolder(
                    ItemOtherUserMessageBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ),
                        parent,
                        false
                    )
                )
            }
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemViewType(position: Int) = getItem(position)!!.viewType!!.ordinal
}

class DiffUtilCallback : DiffUtil.ItemCallback<ChatMessage>() {
    override fun areItemsTheSame(oldChat: ChatMessage, newChat: ChatMessage): Boolean {
        return oldChat.messageId == newChat.messageId
    }

    override fun areContentsTheSame(oldChat: ChatMessage, newChat: ChatMessage): Boolean {
        return oldChat == newChat
    }
}


abstract class MessageViewHolder(binding: ViewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    abstract fun onBind(chat: ChatMessage)
}