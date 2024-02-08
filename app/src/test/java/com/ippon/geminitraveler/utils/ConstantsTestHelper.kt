package com.ippon.geminitraveler.utils

import com.ippon.geminitraveler.core.utils.DataState
import com.ippon.geminitraveler.core.utils.Resource
import com.ippon.geminitraveler.data.mappers.mapToChat
import com.ippon.geminitraveler.data.mappers.mapToChatEntity
import com.ippon.geminitraveler.data.mappers.mapToMessageEntity
import com.ippon.geminitraveler.domain.model.Chat
import com.ippon.geminitraveler.domain.model.ChatRequest
import com.ippon.geminitraveler.domain.model.ModelRequest
import com.ippon.geminitraveler.domain.model.ModelResponse
import com.ippon.geminitraveler.domain.model.Role
import com.ippon.geminitraveler.ui.mapper.mapToChatHistoryItem
import com.ippon.geminitraveler.ui.mapper.mapToModelResponseUi
import com.ippon.geminitraveler.ui.models.ChatUiState
import com.ippon.geminitraveler.ui.models.MessagesUiState
import java.time.Instant

object ConstantsTestHelper {
    const val MODEL_RESPONSE = "response"
    const val MODEL_REQUEST_DATA = "request"
    const val CHAT_TITLE = "chat title"
    val createAt: Instant = Instant.parse("2024-02-06T08:51:17.775268Z")
    const val CHAT_ID = 1L
    const val MESSAGE_USER_ID = 1L

    val modelResponse = ModelResponse(
        data = MODEL_RESPONSE,
        role = Role.MODEL,
        createAt = createAt,
        chatId = CHAT_ID
    )

    val modelRequest = ModelRequest(
        data = MODEL_REQUEST_DATA,
        createAt = createAt,
        chatId = CHAT_ID
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
        messages = uiMessages,
        currentMessageId = MESSAGE_USER_ID
    )
    val errorMessagesUiState = MessagesUiState(
        dataState = DataState.ERROR,
        errorMessage = ERROR_MESSAGE
    )

    val messageEntity = modelResponse.mapToMessageEntity()

    val resourceSuccess: Resource<Long> = Resource.Success(CHAT_ID)
    val resourceError: Resource<Long> = Resource.Error(
        throwable = throwable,
        errorMessage = ERROR_MESSAGE
    )
    val chatRequest = ChatRequest(
        title = CHAT_TITLE,
        createAt = createAt
    )
    val chatEntity = chatRequest.mapToChatEntity()
    private val chat = chatEntity.mapToChat()
    val chats = listOf(chat)
    val chatsEntities = chats.map { it.mapToChatEntity() }

    val resourceSuccessChats: Resource<List<Chat>> = Resource.Success(chats)
    val resourceErrorChats: Resource<List<Chat>> = Resource.Error(
        throwable = throwable,
        errorMessage = ERROR_MESSAGE
    )

    val uiChats = chats.map { it.mapToChatHistoryItem() }

    val initialChatsUiState = ChatUiState()
    val errorChatsUiState = ChatUiState(
        errorMessage = throwable.message,
        dataState = DataState.ERROR
    )
    val successChatsUiState = ChatUiState(
        dataState = DataState.SUCCESS,
        chats = uiChats,
        currentChatId = CHAT_ID
    )
}