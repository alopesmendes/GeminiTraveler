package com.ippon.geminitraveler.di

import com.google.ai.client.generativeai.GenerativeModel
import com.ippon.geminitraveler.BuildConfig
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single


@Module
@ComponentScan("com.ippon.geminitraveler")
class AppModule {
    @Single
    fun provideGenerativeModel() = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = BuildConfig.apiKey
    )
}