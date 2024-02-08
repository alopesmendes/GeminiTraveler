package com.ippon.geminitraveler.domain.repository

import com.ippon.geminitraveler.core.utils.Resource
import com.ippon.geminitraveler.domain.model.ModelResponse
import com.ippon.geminitraveler.domain.model.ModelRequest
import kotlinx.coroutines.flow.Flow

interface MessagesRepository {
    fun getMessagesFromChat(chatId: Long): Flow<Resource<List<ModelResponse>>>

    suspend fun addUserMessage(modelRequest: ModelRequest): Resource<Long>

    suspend fun addModelMessage(modelRequest: ModelRequest): Resource<Long>
}