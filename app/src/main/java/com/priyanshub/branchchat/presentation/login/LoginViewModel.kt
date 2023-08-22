package com.priyanshub.branchchat.presentation.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.priyanshub.branchchat.common.Resource
import com.priyanshub.branchchat.domain.models.login.LoginRequest
import com.priyanshub.branchchat.domain.models.login.LoginResponse
import com.priyanshub.branchchat.domain.repository.CustomerServiceRepository
import com.priyanshub.branchchat.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
): ViewModel() {


    private val _showProgress : MutableLiveData<Boolean> = MutableLiveData()
    val showProgress: LiveData<Boolean>
        get() = _showProgress

    private val _loginResponse : MutableLiveData<LoginResponse?> = MutableLiveData()
    val loginResponse: MutableLiveData<LoginResponse?>
        get() = _loginResponse

    fun login(username: String, password: String) = viewModelScope.launch {
        val loginRequest = LoginRequest(username, password)
        loginUseCase.invoke(loginRequest).collect{
            when(it){
                is Resource.Loading -> {
                    _showProgress.postValue(true)
                }
                is Resource.Error -> {
                    Log.d("PRI", "login error message: ${it.data?.message()}")
                    _loginResponse.postValue(null)
                    _showProgress.postValue(false)
                }
                is Resource.Success -> {
                    Log.d("PRI", "login message: ${it.data?.message()}")
                    Log.d("PRI", "login: ${it.data?.body()}")
                    _loginResponse.postValue(it.data?.body())
                    _showProgress.postValue(false)
                }
            }
        }
    }

}