package com.ippon.geminitraveler.utils

import com.ippon.geminitraveler.core.utils.DataState
import com.ippon.geminitraveler.core.utils.Resource
import com.ippon.geminitraveler.data.mappers.mapToMessageEntity
import com.ippon.geminitraveler.domain.model.ModelRequest
import com.ippon.geminitraveler.domain.model.ModelResponse
import com.ippon.geminitraveler.domain.model.Role
import com.ippon.geminitraveler.ui.mapper.mapToModelResponseUi
import com.ippon.geminitraveler.ui.models.ModelEvent
import com.ippon.geminitraveler.ui.models.MessagesUiState

object ConstantsTestHelper {
    private const val MODEL_RESPONSE = "response"
    const val MODEL_REQUEST_DATA = "request"

    val modelResponse = ModelResponse(
        data = MODEL_RESPONSE,
        role = Role.MODEL
    )

    val modelRequest = ModelRequest(MODEL_REQUEST_DATA)

    private const val ERROR_MESSAGE = "error"
    val throwable = IllegalStateException(ERROR_MESSAGE)

    val responses = listOf(modelResponse)

    val messagesEntities = responses.map { it.mapToMessageEntity() }

    val uiMessages = responses.map { it.mapToModelResponseUi() }

    val resourceLoadingMessages: Resource<List<ModelResponse>> = Resource.Loading
    val resourceSuccessMessages: Resource<List<ModelResponse>> = Resource.Success(responses)
    val resourceErrorMessages: Resource<List<ModelResponse>> = Resource.Error(
        errorMessage = ERROR_MESSAGE,
        throwable = throwable
    )

    val initialMessagesUiState = MessagesUiState()
    val successMessagesUiState = MessagesUiState(
        dataState = DataState.SUCCESS,
        messages = uiMessages
    )
    val errorMessagesUiState = MessagesUiState(
        dataState = DataState.ERROR,
        errorMessage = ERROR_MESSAGE
    )

    val userSendMessage = ModelEvent.UserSendMessage(
        prompt = MODEL_REQUEST_DATA
    )
    val getMessages = ModelEvent.GetMessages

    val messageEntity = modelResponse.mapToMessageEntity()

    val resourceSuccess: Resource<Unit> = Resource.Success(Unit)

    val resourceError: Resource<Unit> = Resource.Error(
        throwable = throwable,
        errorMessage = ERROR_MESSAGE
    )
}