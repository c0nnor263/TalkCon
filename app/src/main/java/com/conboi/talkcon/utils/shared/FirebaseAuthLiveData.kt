package com.conboi.talkcon.utils.shared

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FirebaseAuthLiveData : LiveData<FirebaseUser?>() {
    enum class AuthState {
        AUTHENTICATED, UNAUTHENTICATED
    }

    private val auth = Firebase.auth
    private val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        value = firebaseAuth.currentUser
    }

    override fun onActive() {
        auth.addAuthStateListener(authListener)
    }

    override fun onInactive() {
        auth.removeAuthStateListener(authListener)
    }
}