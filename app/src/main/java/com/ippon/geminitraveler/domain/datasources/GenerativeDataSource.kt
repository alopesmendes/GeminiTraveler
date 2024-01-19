package com.ippon.geminitraveler.domain.datasources

interface GenerativeDataSource {
    suspend fun generateContent(prompt: String): String?
}