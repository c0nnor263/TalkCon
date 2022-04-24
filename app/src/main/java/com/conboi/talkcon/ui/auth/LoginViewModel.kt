package com.conboi.talkcon.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.conboi.talkcon.data.source.remote.repo.FirebaseRepository
import com.google.firebase.auth.PhoneAuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val firebaseRepository: FirebaseRepository,
) : ViewModel() {
    val verifySignInState = MutableLiveData(false)

    private val _bufferPhoneNumber = MutableLiveData("")
    val bufferPhoneNumber: LiveData<String> = _bufferPhoneNumber

    private val _bufferVerificationCode = MutableLiveData<Int>()
    val bufferVerificationCode: LiveData<Int> = _bufferVerificationCode


    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential, callback: () -> Unit) {
        firebaseRepository.signInWithPhoneAuthCredential(credential, callback)
    }

    fun updateVerifySignIn(state: Boolean) {
        verifySignInState.value = state
    }

    fun updateBufferPhoneNumber(value: String) {
        _bufferPhoneNumber.value = value
    }

    fun updateBufferVerificationCode(value: Int) {
        _bufferVerificationCode.value = value
    }

    fun saveState() {
        savedStateHandle.apply {
            set(VERIFY_SIGN_IN, verifySignInState.value)
            set(BUFFER_PHONE_NUMBER, bufferPhoneNumber.value)
            set(BUFFER_VERIFICATION_CODE, bufferVerificationCode.value)
        }
    }

    fun retrieveState() {
        savedStateHandle.apply {
            getLiveData<Boolean>(VERIFY_SIGN_IN).value?.let {
                updateVerifySignIn(it)
            }
            getLiveData<String>(BUFFER_PHONE_NUMBER).value?.let {
                updateBufferPhoneNumber(
                    it
                )
            }
            getLiveData<Int>(BUFFER_VERIFICATION_CODE).value?.let {
                updateBufferVerificationCode(
                    it
                )
            }
        }
    }

    companion object {
        const val VERIFY_SIGN_IN = "verifySignIn"
        const val BUFFER_PHONE_NUMBER = "bufferPhoneNumber"
        const val BUFFER_VERIFICATION_CODE = "bufferVerificationCode"
    }
}