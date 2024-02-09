package com.ippon.geminitraveler.data.datasource.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.ippon.geminitraveler.data.datasource.database.entities.MessageEntity
import com.ippon.geminitraveler.data.datasource.database.entities.MessageWithMessagesEntities
import kotlinx.coroutines.flow.Flow

@Dao
abstract class MessageDao: BaseDao<MessageEntity> {
    @Query("SELECT * FROM messages WHERE chat_id = :chatId")
    abstract fun findAllMessages(chatId: Long): Flow<List<MessageEntity>>
}