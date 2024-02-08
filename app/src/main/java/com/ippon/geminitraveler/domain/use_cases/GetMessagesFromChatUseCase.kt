package com.ippon.geminitraveler.domain.use_cases

import com.ippon.geminitraveler.core.utils.DataState
import com.ippon.geminitraveler.core.utils.Resource
import com.ippon.geminitraveler.domain.repository.MessagesRepository
import com.ippon.geminitraveler.ui.mapper.mapToModelResponseUi
import com.ippon.geminitraveler.ui.models.MessagesUiState
import org.koin.core.annotation.Single

@Single
class GetMessagesFromChatUseCase(
    private val messagesRepository: MessagesRepository
) {
    suspend operator fun invoke(
        chatId: Long,
        updateState: ((MessagesUiState) -> MessagesUiState) -> Unit
    ) {
        messagesRepository
            .getMessagesFromChat(chatId)
            .collect { resource ->
                when (resource) {
                    is Resource.Error -> updateState.invoke { state ->
                        state.copy(
                            dataState = DataState.ERROR,
                            errorMessage = resource.errorMessage
                        )
                    }
                    is Resource.Success -> updateState.invoke { state ->
                        state.copy(
                            messages = resource.data.map { it.mapToModelResponseUi() }
                        )
                    }
                }
            }
    }
}