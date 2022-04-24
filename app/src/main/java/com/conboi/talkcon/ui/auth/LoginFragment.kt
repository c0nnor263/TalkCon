package com.conboi.talkcon.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.conboi.talkcon.R
import com.conboi.talkcon.databinding.FragmentLoginBinding
import com.conboi.talkcon.ui.MainActivity
import com.conboi.talkcon.utils.showErrorToast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    val viewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater)
        binding.tietPhoneNumber.addTextChangedListener {
            if (it.toString().isNotBlank()) {
                viewModel.updateBufferPhoneNumber(it.toString())
                binding.mBtnConfirm.visibility = View.VISIBLE
            }
        }
        binding.tietVerification.addTextChangedListener {
            if (it.toString().isNotBlank()) {
                viewModel.updateBufferVerificationCode(it.toString().toInt())
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        binding.layoutViewModel = viewModel
        if (viewModel.verifySignInState.value == true) {
            showSignIn()
        }
        initLoginUI()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.saveState()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        viewModel.retrieveState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initLoginUI() {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d("TAG", "onCodeSent: success")
                _binding?.let {
                    binding.mBtnSignIn.setOnClickListener {
                        val credential =
                            PhoneAuthProvider.getCredential(
                                verificationId,
                                binding.tietVerification.text.toString()
                            )
                        viewModel.signInWithPhoneAuthCredential(credential) {
                            successSignIn()
                        }
                    }
                }

            }

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d("TAG", "onVerificationCompleted: success")
                viewModel.signInWithPhoneAuthCredential(credential) {
                    successSignIn()
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.d("TAG", "onVerificationFailed: success")
                showErrorToast(requireContext(), e)
            }
        }
        binding.mBtnConfirm.setOnClickListener {
            val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                .setPhoneNumber(binding.tietPhoneNumber.text.toString())
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(requireActivity() as MainActivity)
                .setCallbacks(callbacks)
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
            showSignIn()
        }
    }


    private fun showSignIn() {
        viewModel.bufferVerificationCode.value?.let { binding.tietVerification.setText(it) }
        viewModel.updateVerifySignIn(true)

        binding.mBtnConfirm.visibility = View.GONE
        binding.tilVerification.visibility = View.VISIBLE
        binding.mBtnSignIn.visibility = View.VISIBLE
    }

    private fun successSignIn() {
        findNavController().popBackStack()
        findNavController().navigate(R.id.mainFragment)
    }

}