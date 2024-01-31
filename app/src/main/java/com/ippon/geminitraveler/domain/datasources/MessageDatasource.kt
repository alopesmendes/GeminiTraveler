package com.ippon.geminitraveler.domain.datasources

import com.ippon.geminitraveler.domain.model.ModelResponse

interface MessageDatasource {
    suspend fun addMessage(message: ModelResponse)
}