package com.ippon.geminitraveler.domain.datasources

import com.ippon.geminitraveler.domain.model.ModelResponse
import kotlinx.coroutines.flow.Flow

interface MessageDatasource {
    suspend fun addMessage(message: ModelResponse)

    fun getMessages(): Flow<List<ModelResponse>>
}