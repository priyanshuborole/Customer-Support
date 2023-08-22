package com.priyanshub.branchchat.presentation.conversation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.priyanshub.branchchat.databinding.ItemMessageAgentBinding
import com.priyanshub.branchchat.databinding.ItemMessageUserBinding
import com.priyanshub.branchchat.domain.models.message.Message
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ConversationAdapter(
    private val messages: ArrayList<Message>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_USER = 0
    private val VIEW_TYPE_AGENT = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_USER -> {
                val binding = ItemMessageUserBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                UserViewHolder(binding)
            }
            VIEW_TYPE_AGENT -> {
                val binding = ItemMessageAgentBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                AgentViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        when (holder) {
            is UserViewHolder -> {
                holder.bind(message)
            }
            is AgentViewHolder -> {
                holder.bind(message)
            }
        }
    }

    fun updateList(messageList: List<Message>){
        this.messages.clear()
        this.messages.addAll(messageList)
        notifyDataSetChanged()
    }

    fun addMessage(message: Message){
        this.messages.add(message)
        notifyDataSetChanged()
    }

    fun reset(){
        val onlyUser = this.messages.filter {it.agent_id == null}
        this.messages.clear()
        this.messages.addAll(onlyUser)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = messages.size

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        return if (message.agent_id == null) {
            VIEW_TYPE_USER
        } else {
            VIEW_TYPE_AGENT
        }
    }

    inner class UserViewHolder( val binding: ItemMessageUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.tvUserId.text = message.user_id.toString()
            binding.tvBody.text = message.body.toString()
            binding.tvTime.text = convertDateTime(message.timestamp)
        }
    }

    inner class AgentViewHolder(val binding: ItemMessageAgentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.tvAgentId.text = message.agent_id.toString()
            binding.tvBody.text = message.body.toString()
            binding.tvTime.text = convertDateTime(message.timestamp)
        }
    }

    private fun convertDateTime(dateTimeString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())

        val date = inputFormat.parse(dateTimeString)
        return date?.let { outputFormat.format(it) }.toString()
    }
}
