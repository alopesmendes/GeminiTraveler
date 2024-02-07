package com.ippon.geminitraveler.utils

import com.ippon.geminitraveler.core.utils.DataState
import com.ippon.geminitraveler.core.utils.Resource
import com.ippon.geminitraveler.data.mappers.mapToChatEntity
import com.ippon.geminitraveler.data.mappers.mapToMessageEntity
import com.ippon.geminitraveler.domain.model.Chat
import com.ippon.geminitraveler.domain.model.ModelRequest
import com.ippon.geminitraveler.domain.model.ModelResponse
import com.ippon.geminitraveler.domain.model.Role
import com.ippon.geminitraveler.ui.mapper.mapToModelResponseUi
import com.ippon.geminitraveler.ui.models.MessagesUiState
import java.time.Instant

object ConstantsTestHelper {
    const val MODEL_RESPONSE = "response"
    const val MODEL_REQUEST_DATA = "request"
    private val createAt = Instant.parse("2024-02-06T08:51:17.775268Z")

    val modelResponse = ModelResponse(
        data = MODEL_RESPONSE,
        role = Role.MODEL,
        createAt = createAt
    )

    val modelRequest = ModelRequest(
        data = MODEL_REQUEST_DATA,
        createAt = createAt
    )

    private const val ERROR_MESSAGE = "error"
    val throwable = IllegalStateException(ERROR_MESSAGE)

    val responses = listOf(modelResponse)

    val messagesEntities = responses.map { it.mapToMessageEntity() }

    val uiMessages = responses.map { it.mapToModelResponseUi() }

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

    val messageEntity = modelResponse.mapToMessageEntity()

    val resourceSuccess: Resource<Unit> = Resource.Success(Unit)
    val resourceError: Resource<Unit> = Resource.Error(
        throwable = throwable,
        errorMessage = ERROR_MESSAGE
    )

    val chat = Chat(
        id = 0,
        createAt = createAt,
        title = "Title"
    )
    val chatEntity = chat.mapToChatEntity()
    val chats = listOf(chat)
    val chatsEntities = chats.map { it.mapToChatEntity() }

    val resourceSuccessChats: Resource<List<Chat>> = Resource.Success(chats)
    val resourceErrorChats: Resource<List<Chat>> = Resource.Error(
        throwable = throwable,
        errorMessage = ERROR_MESSAGE
    )
}