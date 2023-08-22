package com.priyanshub.branchchat.domain.usecase

import com.priyanshub.branchchat.common.Resource
import com.priyanshub.branchchat.domain.models.login.LoginRequest
import com.priyanshub.branchchat.domain.models.login.LoginResponse
import com.priyanshub.branchchat.domain.repository.CustomerServiceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: CustomerServiceRepository
) {
    operator fun invoke(
        loginRequest: LoginRequest
    ): Flow<Resource<Response<LoginResponse>>> = flow {
        try {
            emit(Resource.Loading())
            val response = repository.login(loginRequest)
            emit(Resource.Success(response))
        } catch (e: Exception)
        {
            e.printStackTrace()
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred in login"))
        }
    }
}