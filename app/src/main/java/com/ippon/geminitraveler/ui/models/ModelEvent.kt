package com.ippon.geminitraveler.ui.models

sealed interface ModelEvent {
    data class ModelRequestEvent(
        val prompt: String
    ): ModelEvent
}