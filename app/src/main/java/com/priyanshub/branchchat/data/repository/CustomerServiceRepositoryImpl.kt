package com.priyanshub.branchchat.data.repository

import com.priyanshub.branchchat.data.remote.CustomerServiceRequest
import com.priyanshub.branchchat.domain.models.login.LoginRequest
import com.priyanshub.branchchat.domain.models.login.LoginResponse
import com.priyanshub.branchchat.domain.models.message.Message
import com.priyanshub.branchchat.domain.models.message.MessageRequest
import com.priyanshub.branchchat.domain.repository.CustomerServiceRepository
import retrofit2.Response

class CustomerServiceRepositoryImpl(
    private val api: CustomerServiceRequest
): CustomerServiceRepository {
    override suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> {
        return api.login(loginRequest)
    }

    override suspend fun getMessages(authToken: String):List<Message>{
        return api.getMessages(authToken)
    }

    override suspend fun sendMessages(authToken: String, messageRequest: MessageRequest): Message {
        return api.sendMessage(authToken, messageRequest)
    }

    override suspend fun reset(authToken: String) {
        return api.reset(authToken)
    }

}