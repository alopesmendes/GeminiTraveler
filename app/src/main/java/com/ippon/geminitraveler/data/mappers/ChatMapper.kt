package com.ippon.geminitraveler.data.mappers

import com.ippon.geminitraveler.data.datasource.database.entities.ChatEntity
import com.ippon.geminitraveler.domain.model.Chat

fun ChatEntity.mapToChat(): Chat = Chat(
    id = id,
    title = title,
    createAt = createAt
)

fun Chat.mapToChatEntity(): ChatEntity = ChatEntity(
    id = id,
    title = title,
    createAt = createAt
)