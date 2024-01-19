package com.ippon.geminitraveler.ui.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ippon.geminitraveler.core.utils.State
import com.ippon.geminitraveler.domain.model.PlanTravel
import com.ippon.geminitraveler.domain.use_cases.GetPlanTravelUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlanTravelViewModel(
    private val getPlanTravelUseCase: GetPlanTravelUseCase
): ViewModel() {
    private val _uiState: MutableStateFlow<State<PlanTravel>> = MutableStateFlow(State.Initial)
    val uiState = _uiState.asStateFlow()

    fun requestPlanTravel(prompt: String) {
        viewModelScope.launch {
            val response = getPlanTravelUseCase(prompt)
            response.collect { newState ->
                _uiState.update { newState }
            }
        }
    }
}