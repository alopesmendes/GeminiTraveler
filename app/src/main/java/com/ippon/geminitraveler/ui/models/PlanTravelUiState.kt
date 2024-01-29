package com.ippon.geminitraveler.ui.models

import androidx.compose.runtime.Immutable
import com.ippon.geminitraveler.core.utils.DataState
import com.ippon.geminitraveler.core.utils.UiState
import com.ippon.geminitraveler.domain.model.PlanTravel

@Immutable
data class PlanTravelUiState(
    override val dataState: DataState = DataState.INITIAL,
    val planTravels: List<PlanTravel> = emptyList(),
    val errorMessage: String? = null,
): UiState {
    operator fun plus(uiState: PlanTravelUiState): PlanTravelUiState {
        return copy(
            planTravels = planTravels + uiState.planTravels
        )
    }
}
