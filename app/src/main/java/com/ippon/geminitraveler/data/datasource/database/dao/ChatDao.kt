package com.ippon.geminitraveler.data.datasource.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import com.ippon.geminitraveler.core.utils.Constants
import com.ippon.geminitraveler.data.datasource.database.entities.ChatEntity
import com.ippon.geminitraveler.data.datasource.database.entities.ChatEntityId
import com.ippon.geminitraveler.data.datasource.database.entities.ChatEntityTitle
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ChatDao: BaseDao<ChatEntity> {
    @Query("SELECT * FROM ${Constants.CHATS_TABLE}")
    abstract fun findAllChats(): Flow<List<ChatEntity>>

    @Delete(ChatEntity::class)
    abstract suspend fun deleteById(id: ChatEntityId)

    @Update(ChatEntity::class)
    abstract suspend fun updateTitle(chatEntityTitle: ChatEntityTitle)
}