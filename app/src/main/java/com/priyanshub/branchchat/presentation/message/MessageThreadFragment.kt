package com.priyanshub.branchchat.presentation.message

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
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
    lateinit var snackBarExitConfirmation: Snackbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMessageThreadBinding.inflate(layoutInflater, container, false)

        val sharedPreference =  requireActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val token = sharedPreference.getString(Constants.TOKEN,"")
        viewModel.getMessages(token.toString())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val snackbar = Constants.snackBarTemplate(binding.root,"Fetching Thread....")
        snackbar.show()

        setupRecyclerView(emptyList())

        viewModel.getMessageResponse.observe(viewLifecycleOwner){list->
            if (list.isEmpty()){
                binding.btnRetry.visibility = View.VISIBLE
                Toast.makeText(requireContext(), "Issue In Fetching Message Thread", Toast.LENGTH_SHORT).show()
                snackbar.dismiss()
            }
            else {
                messageThreadAdapter.updateList(list)
                binding.btnRetry.visibility = View.GONE
                snackbar.dismiss()
            }
        }

        viewModel.showProgress.observe(viewLifecycleOwner){showProgress->
            binding.paginationProgressBar.visibility = if (showProgress) View.VISIBLE else View.GONE

        }

        binding.btnRetry.setOnClickListener {
            binding.btnRetry.visibility = View.GONE
            snackbar.show()
            val sharedPreference =  requireActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE)
            val token = sharedPreference.getString(Constants.TOKEN,"")
            viewModel.getMessages(token.toString())
        }
        setUpBackPress()
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

    private fun setUpBackPress() {
        snackBarExitConfirmation = Snackbar.make(
            requireActivity().window.decorView.rootView,
            "Please press BACK again to exit",
            Snackbar.LENGTH_LONG
        )

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!snackBarExitConfirmation.isShown) {
                    snackBarExitConfirmation.show()
                } else {
                    requireActivity().finish()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

}