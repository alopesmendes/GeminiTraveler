package com.ippon.geminitraveler.domain.datasources

import com.ippon.geminitraveler.domain.model.ModelResponse
import kotlinx.coroutines.flow.Flow

interface MessageDatasource {
    suspend fun insertMessage(message: ModelResponse): Long

    /***
     * Gets messages from [chatId]
     *
     * @param chatId the id from a chat
     * @return a flow of [ModelResponse]
     */
    fun getMessagesFromChat(chatId: Long): Flow<List<ModelResponse>>
}