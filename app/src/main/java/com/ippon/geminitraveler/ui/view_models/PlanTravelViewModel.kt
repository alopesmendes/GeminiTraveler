package com.ippon.geminitraveler.ui.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ippon.geminitraveler.domain.use_cases.GetPlanTravelUseCase
import com.ippon.geminitraveler.ui.models.PlanTravelEvent
import com.ippon.geminitraveler.ui.models.PlanTravelUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class PlanTravelViewModel(
    private val getPlanTravelUseCase: GetPlanTravelUseCase
): ViewModel() {
    private val _uiState: MutableStateFlow<PlanTravelUiState> = MutableStateFlow(PlanTravelUiState())
    val uiState = _uiState.asStateFlow()

    private val events = Channel<PlanTravelEvent>()

    init {
        loadEvents()
    }

    fun onHandleEvent(event: PlanTravelEvent) {
        viewModelScope.launch {
            events.trySend(event)
        }
    }

    private fun loadEvents() {
        viewModelScope.launch {
            events
                .receiveAsFlow()
                .collect { event ->
                    when (event) {
                        is PlanTravelEvent.ModelRequestEvent -> {
                            getPlanTravelUseCase(
                                prompt = event.prompt,
                                initialUiState = _uiState.value
                            ).collect { newState ->
                                _uiState.update { newState }
                            }
                        }
                    }

                }
        }
    }
}