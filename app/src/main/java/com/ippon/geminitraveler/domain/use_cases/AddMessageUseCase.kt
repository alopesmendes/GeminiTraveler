package com.ippon.geminitraveler.domain.use_cases

import com.ippon.geminitraveler.core.utils.DataState
import com.ippon.geminitraveler.core.utils.Resource
import com.ippon.geminitraveler.domain.model.ModelRequest
import com.ippon.geminitraveler.domain.repository.MessagesRepository
import com.ippon.geminitraveler.ui.models.MessagesUiState
import org.koin.core.annotation.Single
import java.time.Instant

@Single
class AddMessageUseCase(
    private val messagesRepository: MessagesRepository
) {
    suspend operator fun invoke(
        prompt: String,
        createAt: Instant = Instant.now(),
        updateState: ((MessagesUiState) -> MessagesUiState) -> Unit
    ) {
        val modelRequest = ModelRequest(
            data = prompt,
            createAt = createAt
        )

        // Update user state message
        updateState.invoke { state -> state.copy(dataState = DataState.LOADING) }
        val userResource = messagesRepository.addUserMessage(modelRequest)
        updateState.invoke { state -> userResource.handleResource(state) }

        // Update model state message
        updateState.invoke { state -> state.copy(dataState = DataState.LOADING) }
        val modelResource = messagesRepository.addModelMessage(modelRequest)
        updateState.invoke { state -> modelResource.handleResource(state) }
    }



    private fun Resource<Unit>.handleResource(uiState: MessagesUiState): MessagesUiState {
        return when(this) {
            is Resource.Error -> uiState.copy(
                dataState = DataState.ERROR,
                errorMessage = errorMessage
            )
            is Resource.Success -> uiState.copy(
                dataState = DataState.SUCCESS,
            )
        }
    }
}