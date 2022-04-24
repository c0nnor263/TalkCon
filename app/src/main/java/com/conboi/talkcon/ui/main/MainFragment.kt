package com.conboi.talkcon.ui.main

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Slide
import com.conboi.talkcon.adapter.UserChatsAdapter
import com.conboi.talkcon.data.model.UserChat
import com.conboi.talkcon.databinding.FragmentMainBinding
import com.conboi.talkcon.interfaces.ListInterface
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment(), ListInterface {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough().apply {
            duration = 300.toLong()
        }
        exitTransition = MaterialFadeThrough().apply {
            duration = 300.toLong()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(layoutInflater)
        binding.cvOpenMenu.setOnClickListener {
            navigateToMenu()
        }
        binding.fBtnAddChat.setOnClickListener {
            viewModel.testAddNewUserChat()
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.isMenuOpen.value == true) {
            reenterTransition = MaterialFadeThrough().apply {
                duration = 300.toLong()
            }
            viewModel.updateMenuState(false)
        }
        if (viewModel.firebaseRepository.getCurrentUser() != null) {
            initRv()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClickItem(userChat: UserChat) {
        navigateToChat(userChat)
    }


    private fun initRv() {
        val chatAdapter = UserChatsAdapter(this)
        binding.rvChats.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(requireContext()).apply {
                isSmoothScrollbarEnabled = true
            }
            setHasFixedSize(true)
        }

        viewModel.getQueryUsersChats().addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatAdapter.refresh()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        lifecycleScope.launch {
            viewModel.userChatsFlow.collect {
                chatAdapter.submitData(it)
            }
        }
    }

    private fun navigateToMenu() {
        exitTransition = Slide(Gravity.BOTTOM).apply {
            duration = 300.toLong()
        }
        reenterTransition = Slide(Gravity.BOTTOM).apply {
            duration = 300.toLong()
        }
        findNavController().navigate(MainFragmentDirections.actionMainFragmentToMenuFragment())
    }

    private fun navigateToChat(userChat: UserChat) {
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true).apply {
            duration = 300.toLong()
        }
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false).apply {
            duration = 300.toLong()
        }
        findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToChatFragment(
                userChat
            )
        )
    }

}