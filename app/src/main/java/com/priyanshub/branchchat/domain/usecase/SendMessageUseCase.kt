package com.priyanshub.branchchat.domain.usecase

import com.priyanshub.branchchat.domain.models.message.Message
import com.priyanshub.branchchat.domain.models.message.MessageRequest
import com.priyanshub.branchchat.domain.repository.CustomerServiceRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repository: CustomerServiceRepository
) {
    suspend operator fun invoke(authToken: String,messageRequest: MessageRequest): Message {
        return try {
            repository.sendMessages(authToken,messageRequest)
        } catch (e: Exception) {
            e.printStackTrace()
            Message(0,0, timestamp = "")
        }
    }
}