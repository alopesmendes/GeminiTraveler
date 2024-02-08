package com.ippon.geminitraveler.ui.models

sealed interface ChatEvent {
    data class SelectChat(
        val chatId: Long,
    ): ChatEvent

    data object CreateNewChat: ChatEvent
}