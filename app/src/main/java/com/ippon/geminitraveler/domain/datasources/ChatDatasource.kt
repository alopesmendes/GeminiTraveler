package com.ippon.geminitraveler.domain.datasources

import com.ippon.geminitraveler.domain.model.Chat
import kotlinx.coroutines.flow.Flow

interface ChatDatasource {
    fun getChats(): Flow<List<Chat>>

    suspend fun insert(chat: Chat)
}