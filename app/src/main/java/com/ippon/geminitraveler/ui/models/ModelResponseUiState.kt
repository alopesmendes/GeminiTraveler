package com.ippon.geminitraveler.ui.models

import androidx.compose.runtime.Immutable
import com.ippon.geminitraveler.core.utils.DataState
import com.ippon.geminitraveler.core.utils.UiState

@Immutable
data class ModelResponseUiState(
    override val dataState: DataState = DataState.INITIAL,
    val planTravels: List<ModelResponseUi> = emptyList(),
    val errorMessage: String? = null,
): UiState {
    operator fun plus(uiState: ModelResponseUiState): ModelResponseUiState {
        return copy(
            planTravels = planTravels + uiState.planTravels
        )
    }
}
