package com.priyanshub.branchchat.presentation.message

import androidx.lifecycle.ViewModel
import com.priyanshub.branchchat.domain.models.message.MessageRequest
import com.priyanshub.branchchat.domain.usecase.MessageThreadUseCase
import com.priyanshub.branchchat.domain.usecase.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MessageThreadViewModel @Inject constructor(
    private val messageThreadUseCase: MessageThreadUseCase
): ViewModel() {
    suspend fun getMessages(authToken: String) = messageThreadUseCase.invoke(authToken)
}