package com.ippon.geminitraveler.data.repository

import com.ippon.geminitraveler.core.utils.Resource
import com.ippon.geminitraveler.domain.datasources.ChatDatasource
import com.ippon.geminitraveler.domain.model.Chat
import com.ippon.geminitraveler.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class ChatRepositoryImpl(
    private val chatDatasource: ChatDatasource
): ChatRepository {
    override fun getChats(): Flow<Resource<List<Chat>>> = flow {
        try {
            val chats = chatDatasource.getChats()
            emitAll(
                chats.map { value -> Resource.Success(value) }
            )
        } catch (e: Exception) {
            emit(
                Resource.Error(
                    throwable = e,
                    errorMessage = e.message
                )
            )
        }
    }

    override suspend fun addChat(chat: Chat): Resource<Unit> {
        return try {
            chatDatasource.insert(chat)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(
                errorMessage = e.message,
                throwable = e
            )
        }
    }

}