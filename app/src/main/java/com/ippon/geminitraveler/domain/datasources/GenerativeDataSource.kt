package com.ippon.geminitraveler.domain.datasources

import kotlinx.coroutines.flow.Flow

interface GenerativeDataSource {
    suspend fun generateContent(prompt: String): String?

    fun generateContentStream(prompt: String): Flow<String>
}