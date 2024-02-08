package com.ippon.geminitraveler.data.repository

import com.ippon.geminitraveler.core.utils.Resource
import com.ippon.geminitraveler.data.mappers.mapToModelResponse
import com.ippon.geminitraveler.domain.datasources.GenerativeDataSource
import com.ippon.geminitraveler.domain.datasources.MessageDatasource
import com.ippon.geminitraveler.domain.model.ModelRequest
import com.ippon.geminitraveler.domain.model.ModelResponse
import com.ippon.geminitraveler.domain.model.Role
import com.ippon.geminitraveler.domain.repository.MessagesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single
import java.time.Instant

@Single
class MessagesRepositoryImpl(
    private val generativeDataSource: GenerativeDataSource,
    private val messageDatasource: MessageDatasource,
): MessagesRepository {
    override fun getMessagesFromChat(chatId: Long): Flow<Resource<List<ModelResponse>>> = flow {
        try {
            val messages = messageDatasource.getMessagesFromChat(chatId)
            emitAll(messages.map { Resource.Success(it) })
        } catch (e: Exception) {
            emit(
                Resource.Error(
                    errorMessage = e.message,
                    throwable = e
                )
            )
        }
    }

    override suspend fun addUserMessage(modelRequest: ModelRequest): Resource<Long> {
        return try {
            val id = messageDatasource.insertMessage(modelRequest.mapToModelResponse())
            Resource.Success(id)
        } catch (e: Exception) {
            Resource.Error(
                errorMessage = e.message,
                throwable = e
            )
        }
    }

    override suspend fun addModelMessage(modelRequest: ModelRequest): Resource<Long> {
        return try {
            val generateContent = generativeDataSource.generateContent(modelRequest.data)
            val id = messageDatasource.insertMessage(
                ModelResponse(
                    data = generateContent ?: "",
                    role = Role.MODEL,
                    createAt = Instant.now(),
                    chatId = modelRequest.chatId,
                )
            )
            Resource.Success(id)
        } catch (e: Exception) {
            Resource.Error(
                errorMessage = e.message,
                throwable = e
            )
        }
    }
}