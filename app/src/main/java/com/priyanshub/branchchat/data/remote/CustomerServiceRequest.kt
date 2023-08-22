package com.priyanshub.branchchat.data.remote

import com.priyanshub.branchchat.domain.models.login.LoginRequest
import com.priyanshub.branchchat.domain.models.login.LoginResponse
import com.priyanshub.branchchat.domain.models.message.MessageRequest
import com.priyanshub.branchchat.domain.models.message.Message
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface CustomerServiceRequest {

    @POST("api/login")
    suspend fun login(
        @Body  request: LoginRequest
    ) : Response<LoginResponse>

    @GET("api/messages")
    suspend fun getMessages(@Header("X-Branch-Auth-Token") authToken: String) : List<Message>


    @POST("api/messages")
    suspend fun sendMessage(
        @Header("X-Branch-Auth-Token") authToken: String,
        @Body  request: MessageRequest
    ) : Message

    @POST("api/reset")
    suspend fun reset(@Header("X-Branch-Auth-Token") authToken: String)
}