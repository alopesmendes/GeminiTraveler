package com.ippon.geminitraveler.data.datasource

import com.ippon.geminitraveler.data.datasource.database.dao.MessageDao
import com.ippon.geminitraveler.data.mappers.mapToMessageEntity
import com.ippon.geminitraveler.data.mappers.mapToModelResponse
import com.ippon.geminitraveler.domain.datasources.MessageDatasource
import com.ippon.geminitraveler.domain.model.ModelResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class MessageLocalDatasource(
    private val messageDao: MessageDao
): MessageDatasource {
    override suspend fun insertMessage(message: ModelResponse): Long {
        val messageEntity = message.mapToMessageEntity()
        return messageDao.insert(messageEntity)
    }

    override fun getMessagesFromChat(chatId: Long): Flow<List<ModelResponse>> {
        return messageDao.findAllMessages(chatId).map { messageEntities ->
            messageEntities.map { it.mapToModelResponse() }
        }
    }
}