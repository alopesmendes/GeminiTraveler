package com.ippon.geminitraveler.ui.mapper

import com.ippon.geminitraveler.domain.model.Chat
import com.ippon.geminitraveler.domain.model.ModelResponse
import com.ippon.geminitraveler.domain.model.Role
import com.ippon.geminitraveler.ui.models.ChatHistoryItem
import com.ippon.geminitraveler.ui.models.MessageUi
import com.ippon.geminitraveler.ui.models.RoleUi

fun ModelResponse.mapToModelResponseUi(): MessageUi = MessageUi(
    data = data,
    role = role.mapToRoleUi()
)

fun Role.mapToRoleUi(): RoleUi {
    return when(this) {
        Role.USER -> RoleUi.USER
        Role.MODEL -> RoleUi.MODEL
    }
}

fun Chat.mapToChatHistoryItem(): ChatHistoryItem = ChatHistoryItem(
    id = id,
    title = title,
    createAt = createAt.toString()
)