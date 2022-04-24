package com.conboi.talkcon.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.conboi.talkcon.data.model.UserChat
import com.conboi.talkcon.databinding.ItemChatBinding
import com.conboi.talkcon.interfaces.ListInterface

class UserChatsAdapter(
    private val listener: ListInterface
) : PagingDataAdapter<UserChat, UserChatsAdapter.ViewHolder>(UserChatsDiffUtilCallback()) {

    inner class ViewHolder(private var binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val selectedChat = getItem(position) ?: return@setOnClickListener
                    listener.onClickItem(selectedChat)
                }
            }

        }

        fun bind(userChat: UserChat) {
            binding.textPreviewMsg.text = userChat.lastMsg?.textMessage
            binding.accountName.text = userChat.accountName
            binding.avatarImg.load(userChat.avatarUrl)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemChatBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.bind(item)
    }
}

class UserChatsDiffUtilCallback : DiffUtil.ItemCallback<UserChat>() {
    override fun areItemsTheSame(oldItem: UserChat, newItem: UserChat): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UserChat, newItem: UserChat): Boolean {
        return oldItem == newItem
    }
}