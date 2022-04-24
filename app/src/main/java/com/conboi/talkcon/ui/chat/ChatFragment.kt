package com.conboi.talkcon.ui.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.conboi.talkcon.R
import com.conboi.talkcon.adapter.ChatAdapter
import com.conboi.talkcon.data.model.ChatMessage
import com.conboi.talkcon.databinding.FragmentChatBinding
import com.conboi.talkcon.utils.concatenateTwoUserIds
import com.google.android.material.transition.MaterialSharedAxis
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.DateFormat

@AndroidEntryPoint
class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    val viewModel: ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true).apply {
            duration = 300.toLong()
        }
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false).apply {
            duration = 300.toLong()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(layoutInflater)

        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.toolbar.inflateMenu(R.menu.chat_options)
        binding.toolbar.setOnMenuItemClickListener { selectedItem ->
            when (selectedItem.itemId) {
                R.id.option1 -> {
                    Toast.makeText(requireContext(), "1", Toast.LENGTH_SHORT).show()
                }
                R.id.option2 -> {
                    Toast.makeText(requireContext(), "2", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navigationArgs: ChatFragmentArgs by navArgs()
        val user = navigationArgs.userChat
        viewModel.setUserChat(user)
        initRv(user.id)

        binding.tvName.text = user.accountName
        binding.tvLastOnline.text = DateFormat.getTimeInstance().format(user.lastOnline)


        binding.ibTextSend.setOnClickListener {
            val id = viewModel.getCurrentUser()?.uid!!
            viewModel.sendMessageToUser(
                ChatMessage(
                    chatId = concatenateTwoUserIds(id, user.id)!!,
                    messageId = id + System.currentTimeMillis().toString(),
                    textMessage = binding.tietTextSend.text.toString(),
                    sendTime = System.currentTimeMillis(),
                    sendBy = id
                )
            )
            binding.tietTextSend.text = null
            binding.tietTextSend.clearFocus()
        }
    }


    private fun initRv(userChatId: String) {
        val mAdapter = ChatAdapter()
        binding.recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext()).apply {
                stackFromEnd = true
                isSmoothScrollbarEnabled = true
            }
            setHasFixedSize(true)
        }

        viewModel.getQueryChat().addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mAdapter.refresh()
                Log.d("TAG", "onDataChange: refresh")
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        lifecycleScope.launch {
            viewModel.chatFlow(userChatId).collect {
                mAdapter.submitData(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}