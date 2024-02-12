package com.ippon.geminitraveler.domain.use_cases

import com.ippon.geminitraveler.core.utils.DataState
import com.ippon.geminitraveler.core.utils.Resource
import com.ippon.geminitraveler.domain.repository.ChatRepository
import com.ippon.geminitraveler.ui.models.ChatUiState
import org.koin.core.annotation.Single

@Single
class DeleteChatUseCase(
    private val chatRepository: ChatRepository
) {

    suspend operator fun invoke(
        chatId: Long,
        updateState: ((ChatUiState) -> ChatUiState) -> Unit
    ) {
        updateState.invoke { state -> state.copy(dataState = DataState.LOADING) }

        val resource = chatRepository.deleteChat(chatId)
        updateState.invoke { state -> resource.handle(state) }
    }

    private fun Resource<Long>.handle(state: ChatUiState): ChatUiState {
        return when (this) {
            is Resource.Error -> state.copy(
                dataState = DataState.ERROR,
                errorMessage = errorMessage
            )
            is Resource.Success -> state.copy(
                lastDeleteChatId = data,
                dataState = DataState.SUCCESS
            )
        }
    }
}