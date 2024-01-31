package com.ippon.geminitraveler.data.datasource.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.ippon.geminitraveler.data.datasource.database.entities.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class MessageDao: BaseDao<MessageEntity> {
    @Query("SELECT * FROM messages")
    abstract fun findAllMessages(): Flow<List<MessageEntity>>
}