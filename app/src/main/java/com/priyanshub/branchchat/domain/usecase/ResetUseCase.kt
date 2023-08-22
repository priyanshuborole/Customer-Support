package com.priyanshub.branchchat.domain.usecase

import com.priyanshub.branchchat.domain.models.message.Message
import com.priyanshub.branchchat.domain.models.message.MessageRequest
import com.priyanshub.branchchat.domain.repository.CustomerServiceRepository
import javax.inject.Inject

class ResetUseCase @Inject constructor(
    private val repository: CustomerServiceRepository
) {
    suspend operator fun invoke(authToken: String) : Boolean{
        return try {
            repository.reset(authToken)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}