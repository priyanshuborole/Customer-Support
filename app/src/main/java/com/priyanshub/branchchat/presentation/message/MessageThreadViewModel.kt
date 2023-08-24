package com.priyanshub.branchchat.presentation.message

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.priyanshub.branchchat.domain.models.message.Message
import com.priyanshub.branchchat.domain.models.message.MessageRequest
import com.priyanshub.branchchat.domain.usecase.MessageThreadUseCase
import com.priyanshub.branchchat.domain.usecase.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageThreadViewModel @Inject constructor(
    private val messageThreadUseCase: MessageThreadUseCase
) : ViewModel() {

    private val _showProgress : MutableLiveData<Boolean> = MutableLiveData()
    val showProgress: LiveData<Boolean>
        get() = _showProgress

    private val _getMessageResponse : MutableLiveData<List<Message>> = MutableLiveData()
    val getMessageResponse: MutableLiveData<List<Message>>
        get() = _getMessageResponse

    fun getMessages(authToken: String) = viewModelScope.launch(Dispatchers.IO) {
        _showProgress.postValue(true)
        val messages = messageThreadUseCase.invoke(authToken)
        _getMessageResponse.postValue(messages)
        _showProgress.postValue(false)
    }
}