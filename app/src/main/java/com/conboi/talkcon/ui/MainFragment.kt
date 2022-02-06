package com.conboi.talkcon.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.conboi.talkcon.R
import com.conboi.talkcon.adapter.ChatAdapter
import com.conboi.talkcon.databinding.FragmentMainBinding
import com.conboi.talkcon.model.ItemChat
import com.google.android.material.transition.MaterialSharedAxis


class MainFragment : Fragment(), ChatAdapter.ChatListInterface {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chatAdapter = ChatAdapter(this)

        binding.rvChats.adapter = chatAdapter
        binding.rvChats.layoutManager = LinearLayoutManager(requireContext())
        binding.rvChats.setHasFixedSize(true)

        chatAdapter.submitList(
            listOf(
                ItemChat(
                    "1",
                    ",slg,dglsshawdhahwahahawhashawdhahwahahawhashawdhahwahahawhadglssdgs",
                    "daff11111dawf",
                    "awfdwafadfwafdafwafa"
                ),
                ItemChat(
                    "2",
                    ",slgadwafdshawdhahwahahawhashawdhahwahahawhashawdhahwahahawhawaflssdgs",
                    "daff33333dawf",
                    "awfdwafadfwafdafwafa"
                ),
                ItemChat(
                    "3",
                    ",shawdhahwahahshawdhahwahahawhashawdhahwahahawhashawdhahwahahawhaawhahwhsdgs",
                    "d44444affdawf",
                    "awfdwafadfwafdafwafa"
                ),
                ItemChat(
                    "4",
                    ",shawdhahwahahawhashawdhahwahahawhashawdhahwahahawhashawdhahwahahawhashawdhahwahahawha",
                    "daf22222fdawf",
                    "awfdwafadfwafdafwafa"
                )

            )
        )

        binding.openMenu.setOnClickListener {
            val slidingMenu = SlidingMenu()
            slidingMenu.show(requireActivity().supportFragmentManager, slidingMenu.tag)
        }
    }

    override fun navigateToChat(id: String) {
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true).apply {
            duration = 300.toLong()
        }
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false).apply {
            duration = 300.toLong()
        }
        findNavController().navigate(MainFragmentDirections.actionMainFragmentToChatFragment(id))
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}