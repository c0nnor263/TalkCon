package com.conboi.talkcon.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.conboi.talkcon.utils.shared.FirebaseAuthLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor() : ViewModel() {
    val authState = FirebaseAuthLiveData().map { user ->
        if (user != null) {
            FirebaseAuthLiveData.AuthState.AUTHENTICATED
        } else {
            FirebaseAuthLiveData.AuthState.UNAUTHENTICATED
        }
    }
}