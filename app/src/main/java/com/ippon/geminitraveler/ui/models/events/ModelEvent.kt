package com.ippon.geminitraveler.ui.models.events

sealed interface ModelEvent {
    data class UserSendMessage(
        val prompt: String
    ): ModelEvent

    data class GetMessages(
        val chatId: Long,
    ): ModelEvent
}