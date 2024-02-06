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
    override suspend fun addMessage(message: ModelResponse) {
        val messageEntity = message.mapToMessageEntity()
        messageDao.insert(messageEntity)
    }

    override fun getMessages(): Flow<List<ModelResponse>> {
        return messageDao.findAllMessages().map { messageEntities ->
            messageEntities.map { it.mapToModelResponse() }
        }
    }
}