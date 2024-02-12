package com.ippon.geminitraveler.domain.repository

import com.ippon.geminitraveler.core.utils.Resource
import com.ippon.geminitraveler.domain.model.Chat
import com.ippon.geminitraveler.domain.model.ChatRequest
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getChats(): Flow<Resource<List<Chat>>>

    suspend fun addChat(chat: ChatRequest): Resource<Long>

    suspend fun deleteChat(chatId: Long): Resource<Long>

    suspend fun updateChatTitle(
        id: Long,
        title: String
    ): Resource<Long>
}