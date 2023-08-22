package com.priyanshub.branchchat.domain.repository

import com.priyanshub.branchchat.domain.models.login.LoginRequest
import com.priyanshub.branchchat.domain.models.login.LoginResponse
import com.priyanshub.branchchat.domain.models.message.Message
import com.priyanshub.branchchat.domain.models.message.MessageRequest
import retrofit2.Response
import retrofit2.http.Header

interface CustomerServiceRepository {
    suspend fun login(loginRequest: LoginRequest): Response<LoginResponse>

    suspend fun getMessages(authToken: String): List<Message>

    suspend fun sendMessages(authToken: String, messageRequest: MessageRequest): Message

    suspend fun reset(@Header("X-Branch-Auth-Token") authToken: String)

}