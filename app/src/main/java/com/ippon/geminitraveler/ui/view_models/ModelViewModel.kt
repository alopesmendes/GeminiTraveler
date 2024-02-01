package com.ippon.geminitraveler.ui.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ippon.geminitraveler.domain.use_cases.AddMessageUseCase
import com.ippon.geminitraveler.domain.use_cases.GetMessagesUseCase
import com.ippon.geminitraveler.ui.models.ModelEvent
import com.ippon.geminitraveler.ui.models.MessagesUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ModelViewModel(
    private val addMessageUseCase: AddMessageUseCase,
    private val getMessagesUseCase: GetMessagesUseCase
): ViewModel() {
    private val _uiState: MutableStateFlow<MessagesUiState> = MutableStateFlow(MessagesUiState())
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
                        is ModelEvent.GetMessages -> {
                            getMessagesUseCase(
                                uiState = _uiState.value
                            ).collect { newState ->
                                _uiState.update { newState }
                            }
                        }

                        is ModelEvent.UserSendMessage -> {
                            val newState = addMessageUseCase(
                                prompt = event.prompt,
                                uiState = _uiState.value
                            )
                            _uiState.update { newState }
                        }
                    }

                }
        }
    }
}