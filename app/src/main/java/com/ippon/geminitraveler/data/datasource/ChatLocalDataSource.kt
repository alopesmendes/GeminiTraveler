package com.ippon.geminitraveler.data.datasource

import com.ippon.geminitraveler.data.datasource.database.dao.ChatDao
import com.ippon.geminitraveler.data.mappers.mapToChat
import com.ippon.geminitraveler.data.mappers.mapToChatEntity
import com.ippon.geminitraveler.domain.datasources.ChatDatasource
import com.ippon.geminitraveler.domain.model.Chat
import com.ippon.geminitraveler.domain.model.ChatRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class ChatLocalDataSource(
    private val chatDao: ChatDao
): ChatDatasource {
    override fun getChats(): Flow<List<Chat>> {
        return chatDao.findAllChats().map { chatEntities ->
            chatEntities.map { it.mapToChat() }
        }
    }

    override suspend fun insert(chat: ChatRequest) {
        val chatEntity = chat.mapToChatEntity()
        chatDao.insert(chatEntity)
    }
}