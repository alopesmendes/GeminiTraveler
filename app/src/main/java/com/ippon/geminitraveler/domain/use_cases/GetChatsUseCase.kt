package com.ippon.geminitraveler.domain.use_cases

import com.ippon.geminitraveler.core.utils.DataState
import com.ippon.geminitraveler.core.utils.Resource
import com.ippon.geminitraveler.domain.model.Chat
import com.ippon.geminitraveler.domain.repository.ChatRepository
import com.ippon.geminitraveler.ui.mapper.mapToChatHistoryItem
import com.ippon.geminitraveler.ui.models.ChatUiState
import org.koin.core.annotation.Single

@Single
class GetChatsUseCase(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(
        updateState: ((ChatUiState) -> ChatUiState) -> Unit
    ) {
        chatRepository.getChats().collect { resource ->
            updateState.invoke {
                resource.handle(it)
            }
        }
    }

    private fun Resource<List<Chat>>.handle(state: ChatUiState): ChatUiState {
        return when (this) {
            is Resource.Error -> state.copy(
                dataState = DataState.ERROR,
                errorMessage = errorMessage
            )
            is Resource.Success -> state.copy(
                chats = data.map { it.mapToChatHistoryItem() }
            )
        }
    }
}