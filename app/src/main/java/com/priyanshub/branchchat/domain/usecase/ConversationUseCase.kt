package com.priyanshub.branchchat.domain.usecase

import android.util.Log
import com.priyanshub.branchchat.domain.models.message.Message
import com.priyanshub.branchchat.domain.repository.CustomerServiceRepository
import javax.inject.Inject

class ConversationUseCase @Inject constructor(
    private val repository: CustomerServiceRepository
) {
    suspend operator fun invoke(authToken: String, threadId: Int): List<Message> {
        return try {
            val list = repository.getMessages(authToken)
            val filteredList = list.filter { it.thread_id == threadId }
            val sortedList = filteredList.sortedBy { it.timestamp }
            sortedList
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

}