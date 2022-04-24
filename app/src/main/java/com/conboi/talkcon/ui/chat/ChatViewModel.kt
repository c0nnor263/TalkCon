package com.conboi.talkcon.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.conboi.talkcon.data.model.ChatMessage
import com.conboi.talkcon.data.model.UserChat
import com.conboi.talkcon.data.source.remote.repo.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    val firebaseRepository: FirebaseRepository
) : ViewModel() {
    private val _userChat = MutableLiveData(UserChat())
    val userChat: LiveData<UserChat> = _userChat

    val chatFlow: (String) -> Flow<PagingData<ChatMessage>> = { id ->
        firebaseRepository.getChatMessagePager(id).flow.cachedIn(viewModelScope)
    }

    fun setUserChat(userChat: UserChat) {
        _userChat.value = userChat
    }

    fun sendMessageToUser(message: ChatMessage) {
        firebaseRepository.sendMessageToUser(userChat.value?.id!!, message)
    }

    fun getQueryChat() = firebaseRepository.getQueryChat(userChat.value?.id!!)
    fun getCurrentUser() = firebaseRepository.getCurrentUser()
}