package com.priyanshub.branchchat.presentation.message

import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.priyanshub.branchchat.databinding.ItemMessageLayoutBinding
import com.priyanshub.branchchat.domain.models.message.Message
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MessageThreadAdapter(
    private val messageList: ArrayList<Message>,
    private val listener: ItemClickListener
): RecyclerView.Adapter<MessageThreadAdapter.MessageThreadViewHolder>() {
    inner class MessageThreadViewHolder(val binding: ItemMessageLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageThreadViewHolder {
        val binding = ItemMessageLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MessageThreadViewHolder(binding)
    }

    fun updateList(messageList: List<Message>){
        this.messageList.clear()
        this.messageList.addAll(messageList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = messageList.size

    override fun onBindViewHolder(holder: MessageThreadViewHolder, position: Int) {
        val message = messageList[position]
        holder.binding.apply {
            tvThreadId.text = message.thread_id.toString()
            tvUserId.text = spannableString("User Id",message.user_id.toString())
            tvMsgBody.text = message.body
            tvTimeStamp.text = convertDateTime(message.timestamp)
            holder.itemView.setOnClickListener {
                listener.onItemClick(message)
            }
        }
    }

    private fun convertDateTime(dateTimeString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMMM dd, yyyy, hh:mm:ss a", Locale.getDefault())

        val date = inputFormat.parse(dateTimeString)
        return date?.let { outputFormat.format(it) }.toString()
    }

    private fun spannableString(heading : String, content : String): SpannableString {
        val string = SpannableString("$heading - $content")
        string.setSpan(StyleSpan(Typeface.BOLD), 0, heading.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        string.setSpan(ForegroundColorSpan(Color.BLACK), 0, heading.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return string
    }

}
interface ItemClickListener{
    fun onItemClick(message: Message)
}