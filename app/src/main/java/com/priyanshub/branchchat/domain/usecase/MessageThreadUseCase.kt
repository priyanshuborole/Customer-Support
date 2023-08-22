package com.priyanshub.branchchat.domain.usecase

import android.util.Log
import com.priyanshub.branchchat.domain.models.message.Message
import com.priyanshub.branchchat.domain.repository.CustomerServiceRepository
import javax.inject.Inject

class MessageThreadUseCase @Inject constructor(
    private val repository: CustomerServiceRepository
) {
    suspend operator fun invoke(authToken: String): List<Message> {
        return try {
            val list = repository.getMessages(authToken)

            Log.d("PRI", "invoke: $list")
            val latestTimestampMap = HashMap<Int, String>()
            for (message in list) {
                val threadId = message.thread_id
                val timestamp = message.timestamp

                if (!latestTimestampMap.containsKey(threadId) || timestamp > latestTimestampMap[threadId].toString()) {
                    latestTimestampMap[threadId] = timestamp
                }
            }
            val filteredList = ArrayList<Message>()
            for (message in list) {
                val threadId = message.thread_id
                val timestamp = message.timestamp
                if (timestamp == latestTimestampMap[threadId]) {
                    filteredList.add(message)
                }
            }
            Log.d("PRI", "invoke: filtered list $filteredList")
            val sortedList = filteredList.sortedByDescending { it.timestamp }
            Log.d("PRI", "invoke sorted list : $sortedList")
            sortedList
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }


}