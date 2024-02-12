package com.ippon.geminitraveler.ui.models

import com.ippon.geminitraveler.domain.model.Chat

sealed interface ChatEvent {
    data class SelectChat(
        val chatId: Long,
    ): ChatEvent

    data object CreateNewChat: ChatEvent

    data class ChangeChatTitle(
        val title: String
    ): ChatEvent

    data class DeleteChat(
        val chatId: Long
    ): ChatEvent
}