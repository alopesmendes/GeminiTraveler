package com.ippon.geminitraveler.domain.datasources

import com.ippon.geminitraveler.domain.model.ModelResponse
import kotlinx.coroutines.flow.Flow

interface MessageDatasource {
    suspend fun insertMessage(message: ModelResponse): Long

    fun getMessages(): Flow<List<ModelResponse>>
}