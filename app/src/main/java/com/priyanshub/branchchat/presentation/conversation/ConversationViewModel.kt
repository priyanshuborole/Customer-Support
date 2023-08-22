package com.priyanshub.branchchat.presentation.conversation

import androidx.lifecycle.ViewModel
import com.priyanshub.branchchat.domain.models.message.MessageRequest
import com.priyanshub.branchchat.domain.usecase.ConversationUseCase
import com.priyanshub.branchchat.domain.usecase.MessageThreadUseCase
import com.priyanshub.branchchat.domain.usecase.ResetUseCase
import com.priyanshub.branchchat.domain.usecase.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConversationViewModel @Inject constructor(
    private val conversationUseCase: ConversationUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val resetUseCase: ResetUseCase
): ViewModel() {
    suspend fun getMessagesForThreadId(authToken: String,threadId: Int) = conversationUseCase.invoke(authToken, threadId)

    suspend fun sendMessage(authToken: String,messageRequest: MessageRequest) = sendMessageUseCase.invoke(authToken,messageRequest)

    suspend fun reset(authToken: String): Boolean = resetUseCase.invoke(authToken)
}