package com.ippon.geminitraveler.ui.models.events

sealed interface HomeEvent {
    data object GetGeminiDescription: HomeEvent

    data object GetNextPartDescription: HomeEvent
}