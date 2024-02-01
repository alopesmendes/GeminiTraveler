package com.ippon.geminitraveler.domain.use_cases

import com.ippon.geminitraveler.core.utils.DataState
import com.ippon.geminitraveler.core.utils.Resource
import com.ippon.geminitraveler.domain.model.ModelRequest
import com.ippon.geminitraveler.domain.repository.MessagesRepository
import com.ippon.geminitraveler.ui.models.MessagesUiState

class AddMessageUseCase(
    private val messagesRepository: MessagesRepository
) {
    suspend operator fun invoke(
        prompt: String,
        uiState: MessagesUiState
    ): MessagesUiState {
        val modelRequest = ModelRequest(prompt)
        return when (val resource = messagesRepository.addUserAndModelMessages(modelRequest)) {
            is Resource.Error -> uiState.copy(
                    dataState = DataState.ERROR,
                    errorMessage = resource.errorMessage
                )
            Resource.Loading -> uiState.copy(dataState = DataState.LOADING)
            is Resource.Success -> uiState.copy(
                dataState = DataState.SUCCESS,
            )
        }
    }
}