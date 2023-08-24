package com.priyanshub.branchchat.presentation.conversation

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.priyanshub.branchchat.R
import com.priyanshub.branchchat.common.Constants
import com.priyanshub.branchchat.databinding.FragmentConversationBinding
import com.priyanshub.branchchat.databinding.FragmentMessageThreadBinding
import com.priyanshub.branchchat.domain.models.message.Message
import com.priyanshub.branchchat.domain.models.message.MessageRequest
import com.priyanshub.branchchat.presentation.message.ItemClickListener
import com.priyanshub.branchchat.presentation.message.MessageThreadAdapter
import com.priyanshub.branchchat.presentation.message.MessageThreadViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ConversationFragment : Fragment() {

    private lateinit var binding: FragmentConversationBinding
    private val viewModel: ConversationViewModel by viewModels()
    private lateinit var conversationAdapter: ConversationAdapter
    private val args : ConversationFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentConversationBinding.inflate(layoutInflater,container,false)
        val sharedPreference =  requireActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val token = sharedPreference.getString(Constants.TOKEN,"")
        viewModel.getMessagesForThreadId(token.toString(),args.threadId)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(emptyList())
        val sharedPreference =  requireActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val token = sharedPreference.getString(Constants.TOKEN,"")

        viewModel.getMessageResponse.observe(viewLifecycleOwner){list->
            if (list.isEmpty()){
                Toast.makeText(requireContext(), "Issue In Fetching Data", Toast.LENGTH_SHORT).show()
            }
            else {
                conversationAdapter.updateList(list)
            }
        }

        viewModel.getMessage.observe(viewLifecycleOwner){message->
            conversationAdapter.addMessage(message)
        }

        viewModel.showProgress.observe(viewLifecycleOwner){showProgress->
            binding.progressBar.visibility = if (showProgress) View.VISIBLE else View.GONE
        }

        viewModel.isReset.observe(viewLifecycleOwner){
            if (it) {
                conversationAdapter.reset()
                Toast.makeText(requireContext(), "Cleared Agent's chat", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Not able to reset", Toast.LENGTH_SHORT).show()
            }
        }


        binding.btnSend.setOnClickListener {
            val snackbar = Constants.snackBarTemplate(binding.root,"Sending Message....")
            snackbar.show()
            if (!binding.etReply.text.isNullOrBlank()){
                val messageRequest = MessageRequest(args.threadId, binding.etReply.text.toString())
                viewModel.sendMessage(token.toString(),messageRequest)
                binding.etReply.setText("")
                snackbar.dismiss()
            }
        }

        binding.ivReset.setOnClickListener {
            viewModel.reset(token.toString())
        }
    }
    private fun setupRecyclerView(list: List<Message>) {
        conversationAdapter = ConversationAdapter(ArrayList(list))
        binding.rvConvoThread.apply {
            adapter = conversationAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}