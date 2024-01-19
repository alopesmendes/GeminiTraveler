package com.ippon.geminitraveler.data.datasource

import com.google.ai.client.generativeai.GenerativeModel
import com.ippon.geminitraveler.domain.datasources.GenerativeDataSource

class GenerativeDataSourceImpl(
    private val generativeModel: GenerativeModel
): GenerativeDataSource {
    override suspend fun generateContent(prompt: String): String? {
        return generativeModel.generateContent(prompt).text
    }
}