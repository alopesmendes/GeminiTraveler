package com.ippon.geminitraveler.di

import android.content.Context
import com.google.ai.client.generativeai.GenerativeModel
import com.ippon.geminitraveler.BuildConfig
import com.ippon.geminitraveler.data.datasource.database.GeminiLocalDatabase
import com.ippon.geminitraveler.data.datasource.database.dao.ChatDao
import com.ippon.geminitraveler.data.datasource.database.dao.MessageDao
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

    @Single
    fun provideGeminiLocalDatabase(context: Context): GeminiLocalDatabase {
        return GeminiLocalDatabase.getInstance(context)
    }

    @Single
    fun provideMessageDao(database: GeminiLocalDatabase): MessageDao {
        return database.messageDao()
    }

    @Single
    fun provideChatDao(database: GeminiLocalDatabase): ChatDao {
        return database.chatDao()
    }
}