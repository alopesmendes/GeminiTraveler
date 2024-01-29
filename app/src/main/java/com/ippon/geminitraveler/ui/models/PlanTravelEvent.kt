package com.ippon.geminitraveler.ui.models

sealed interface PlanTravelEvent {
    data class ModelRequestEvent(
        val prompt: String
    ): PlanTravelEvent
}