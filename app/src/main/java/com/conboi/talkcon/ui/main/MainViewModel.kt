package com.conboi.talkcon.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.conboi.talkcon.data.source.remote.repo.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val firebaseRepository: FirebaseRepository
) : ViewModel() {
    var isMenuOpen = MutableLiveData(false)

    val userChatsFlow = firebaseRepository.getUserChatsPager().flow.cachedIn(viewModelScope)

    fun updateMenuState(state: Boolean) {
        isMenuOpen.value = state
    }

    fun testAddNewUserChat() = firebaseRepository.testAddNewUserChat()
    fun getQueryUsersChats()  = firebaseRepository.getQueryUserChats()

}