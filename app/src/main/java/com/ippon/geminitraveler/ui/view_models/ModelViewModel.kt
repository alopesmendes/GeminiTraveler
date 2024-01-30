package com.ippon.geminitraveler.ui.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ippon.geminitraveler.domain.use_cases.GetModelResponseUseCase
import com.ippon.geminitraveler.ui.models.ModelEvent
import com.ippon.geminitraveler.ui.models.ModelResponseUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ModelViewModel(
    private val getPlanTravelUseCase: GetModelResponseUseCase
): ViewModel() {
    private val _uiState: MutableStateFlow<ModelResponseUiState> = MutableStateFlow(ModelResponseUiState())
    val uiState = _uiState.asStateFlow()

    private val events = Channel<ModelEvent>()

    init {
        loadEvents()
    }

    fun onHandleEvent(event: ModelEvent) {
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
                        is ModelEvent.ModelRequestEvent -> {
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