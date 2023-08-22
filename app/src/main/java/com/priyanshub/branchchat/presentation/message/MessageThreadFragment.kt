package com.priyanshub.branchchat.presentation.message

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.priyanshub.branchchat.common.Constants
import com.priyanshub.branchchat.databinding.FragmentMessageThreadBinding
import com.priyanshub.branchchat.domain.models.message.Message
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MessageThreadFragment : Fragment() {

    private val viewModel: MessageThreadViewModel by viewModels()
    private lateinit var binding: FragmentMessageThreadBinding
    private lateinit var messageThreadAdapter: MessageThreadAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMessageThreadBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(emptyList())
    }


    private fun setupRecyclerView(list: List<Message>) {
        messageThreadAdapter = MessageThreadAdapter(ArrayList(list), object : ItemClickListener {
            override fun onItemClick(message: Message) {
                val action = MessageThreadFragmentDirections.actionMessageThreadFragmentToConversationFragment(message.thread_id)
                findNavController().navigate(action)
            }
        })
        binding.rvMessageThread.apply {
            adapter = messageThreadAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onResume() {
        super.onResume()
        val sharedPreference =  requireActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val token = sharedPreference.getString(Constants.TOKEN,"")
        binding.paginationProgressBar.visibility = View.VISIBLE
        val snackbar = Constants.snackBarTemplate(binding.root,"Fetching Thread....")
        snackbar.show()
        lifecycleScope.launch(Dispatchers.IO) {
            val list  =  viewModel.getMessages(token.toString())
            withContext(Dispatchers.Main) {
                if (list.isNotEmpty()) {
                    messageThreadAdapter.updateList(list)
                    binding.paginationProgressBar.visibility = View.GONE
                    snackbar.dismiss()
                } else {
                    Toast.makeText(requireContext(), "Issue In Fetching Data", Toast.LENGTH_SHORT)
                        .show()
                    snackbar.dismiss()
                }
            }
        }
    }
}