package com.ippon.geminitraveler.data.repository

import com.ippon.geminitraveler.core.utils.Resource
import com.ippon.geminitraveler.data.mappers.mapToPlanTravel
import com.ippon.geminitraveler.domain.datasources.GenerativeDataSource
import com.ippon.geminitraveler.domain.datasources.MessageDatasource
import com.ippon.geminitraveler.domain.model.ModelResponse
import com.ippon.geminitraveler.domain.model.ModelRequest
import com.ippon.geminitraveler.domain.model.Role
import com.ippon.geminitraveler.domain.repository.ModelRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single

@Single
class ModelRepositoryImpl(
    private val generativeDataSource: GenerativeDataSource,
    private val messageDatasource: MessageDatasource,
): ModelRepository {

    override fun getPlanTravel(requestPlan: ModelRequest): Flow<Resource<ModelResponse>> = flow {
        try {
            // User Input
            val message = requestPlan.mapToPlanTravel()
            val userResource = Resource.Success(message)
            emit(userResource)

            messageDatasource.addMessage(message)

            // Waiting for AI model response
            emit(Resource.Loading)

            // AI model response
            val promptMessage = generativeDataSource.generateContent(requestPlan.data)
            val modelResponse = ModelResponse(
                data = promptMessage ?: "",
                role = Role.MODEL
            )
            emit(
                Resource.Success(modelResponse)
            )

            messageDatasource.addMessage(modelResponse)
        } catch (e: Exception) {
            emit(
                Resource.Error(
                    errorMessage = e.message,
                    throwable = e
                )
            )
        }
    }
}