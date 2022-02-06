package com.conboi.talkcon.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.conboi.talkcon.databinding.ItemChatBinding
import com.conboi.talkcon.model.ItemChat

class ChatAdapter(private val listener: ChatListInterface) :
    ListAdapter<ItemChat, ChatAdapter.ViewHolder>(
        AsyncDifferConfig.Builder(ChatDiffCallback()).build()
    ) {

    inner class ViewHolder(private var binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val selectedChat = getItem(position)
                    listener.navigateToChat(selectedChat.id)
                }
            }

        }

        fun bind(itemChat: ItemChat) {
            binding.textPreviewMsg.text = itemChat.previewMsg
            binding.accountName.text = itemChat.accountName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemChatBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }


    interface ChatListInterface {
        fun navigateToChat(id: String)
    }
}

class ChatDiffCallback : DiffUtil.ItemCallback<ItemChat>() {
    override fun areItemsTheSame(oldItem: ItemChat, newItem: ItemChat): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ItemChat, newItem: ItemChat): Boolean {
        return oldItem == newItem
    }

}