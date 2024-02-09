package com.ippon.geminitraveler.data.datasource.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class MessageWithMessagesEntities(
    @Embedded
    val messageEntity: MessageEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "message_parent_id"
    )
    val messagesEntities: List<MessageEntity>
)
