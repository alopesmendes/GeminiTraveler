package com.ippon.geminitraveler.ui.models

sealed interface ModelEvent {
    data class UserSendMessage(
        val prompt: String
    ): ModelEvent

    data object GetMessages: ModelEvent
}