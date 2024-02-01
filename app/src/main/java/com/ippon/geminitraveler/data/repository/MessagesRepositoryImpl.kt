package com.ippon.geminitraveler.data.repository

import com.ippon.geminitraveler.core.utils.Resource
import com.ippon.geminitraveler.data.mappers.mapToModelResponse
import com.ippon.geminitraveler.domain.datasources.GenerativeDataSource
import com.ippon.geminitraveler.domain.datasources.MessageDatasource
import com.ippon.geminitraveler.domain.model.ModelResponse
import com.ippon.geminitraveler.domain.model.ModelRequest
import com.ippon.geminitraveler.domain.model.Role
import com.ippon.geminitraveler.domain.repository.MessagesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class MessagesRepositoryImpl(
    private val generativeDataSource: GenerativeDataSource,
    private val messageDatasource: MessageDatasource,
): MessagesRepository {

    override fun getMessages(): Flow<Resource<List<ModelResponse>>> = flow {
        emit(Resource.Loading)
        try {
            val messages = messageDatasource.getMessages()
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

    override suspend fun addUserAndModelMessages(modelRequest: ModelRequest): Resource<Unit> {
        return try {
            messageDatasource.addMessage(modelRequest.mapToModelResponse())

            val content = generativeDataSource.generateContent(modelRequest.data)
            messageDatasource.addMessage(
                ModelResponse(
                    role = Role.MODEL,
                    data = content ?: ""
                )
            )
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(
                errorMessage = e.message,
                throwable = e,
            )
        }

    }
}