package com.ippon.geminitraveler.domain.use_cases

import com.ippon.geminitraveler.core.utils.DataState
import com.ippon.geminitraveler.core.utils.Resource
import com.ippon.geminitraveler.domain.model.Chat
import com.ippon.geminitraveler.domain.model.ChatRequest
import com.ippon.geminitraveler.domain.repository.ChatRepository
import com.ippon.geminitraveler.ui.models.ChatUiState
import com.ippon.geminitraveler.ui.models.MessagesUiState
import org.koin.core.annotation.Single
import java.time.Instant

@Single
class AddChatUseCase(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(
        title: String,
        createAt: Instant = Instant.now(),
        updateState: ((ChatUiState) -> ChatUiState) -> Unit
    ) {
        val chat = ChatRequest(
            title = title,
            createAt = createAt
        )
        updateState.invoke { state -> state.copy(dataState = DataState.LOADING) }
        val resource = chatRepository.addChat(chat)
        updateState.invoke { state ->
            resource.handleResource(state)
        }
    }

    private fun Resource<Unit>.handleResource(state: ChatUiState): ChatUiState {
        return when(this) {
            is Resource.Error -> {
                state.copy(
                    dataState = DataState.ERROR,
                    errorMessage = errorMessage,
                )
            }
            is Resource.Success -> {
                state.copy(
                    dataState = DataState.SUCCESS,
                )
            }
        }
    }
}