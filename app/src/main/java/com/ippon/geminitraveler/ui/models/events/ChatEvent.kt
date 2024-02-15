package com.ippon.geminitraveler.ui.models.events

sealed interface ChatEvent {
    data class SelectChat(
        val chatId: Long,
    ): ChatEvent

    data object CreateNewChat: ChatEvent

    data class ChangeChatTitle(
        val chatId: Long,
        val title: String
    ): ChatEvent

    data class DeleteChat(
        val chatId: Long
    ): ChatEvent
}