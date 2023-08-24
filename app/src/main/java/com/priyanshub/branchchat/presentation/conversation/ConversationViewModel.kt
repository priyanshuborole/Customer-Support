package com.priyanshub.branchchat.presentation.conversation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.priyanshub.branchchat.domain.models.login.LoginResponse
import com.priyanshub.branchchat.domain.models.message.Message
import com.priyanshub.branchchat.domain.models.message.MessageRequest
import com.priyanshub.branchchat.domain.usecase.ConversationUseCase
import com.priyanshub.branchchat.domain.usecase.MessageThreadUseCase
import com.priyanshub.branchchat.domain.usecase.ResetUseCase
import com.priyanshub.branchchat.domain.usecase.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConversationViewModel @Inject constructor(
    private val conversationUseCase: ConversationUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val resetUseCase: ResetUseCase

) : ViewModel() {
    private val _showProgress: MutableLiveData<Boolean> = MutableLiveData()
    val showProgress: LiveData<Boolean>
        get() = _showProgress

    private val _showSentProgress: MutableLiveData<Boolean> = MutableLiveData()
    val showSentProgress: LiveData<Boolean>
        get() = _showSentProgress

    private val _getMessageResponse: MutableLiveData<List<Message>> = MutableLiveData()
    val getMessageResponse: MutableLiveData<List<Message>>
        get() = _getMessageResponse

    private val _getMessage: MutableLiveData<Message> = MutableLiveData()
    val getMessage: MutableLiveData<Message>
        get() = _getMessage

    private val _isReset: MutableLiveData<Boolean> = MutableLiveData()
    val isReset: MutableLiveData<Boolean>
        get() = _isReset


    fun getMessagesForThreadId(authToken: String, threadId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            _showProgress.postValue(true)
            val messages = conversationUseCase.invoke(authToken, threadId)
            _getMessageResponse.postValue(messages)
            _showProgress.postValue(false)
        }

     fun sendMessage(authToken: String, messageRequest: MessageRequest) =
        viewModelScope.launch(Dispatchers.IO) {
            val message = sendMessageUseCase.invoke(authToken, messageRequest)
            _getMessage.postValue(message)
        }

     fun reset(authToken: String) = viewModelScope.launch(Dispatchers.IO) {
        _showSentProgress.postValue(true)
        val reset = resetUseCase.invoke(authToken)
        _isReset.postValue(reset)
        _showSentProgress.postValue(false)
    }


}