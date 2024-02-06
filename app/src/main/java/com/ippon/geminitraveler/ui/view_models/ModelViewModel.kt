package com.ippon.geminitraveler.ui.view_models

 import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ippon.geminitraveler.domain.use_cases.AddMessageUseCase
import com.ippon.geminitraveler.domain.use_cases.GetMessagesUseCase
import com.ippon.geminitraveler.ui.models.MessagesUiState
import com.ippon.geminitraveler.ui.models.ModelEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    init {
        loadMessages()
    }

    fun onHandleEvent(event: ModelEvent) {
        viewModelScope.launch {
            when (event) {
                is ModelEvent.UserSendMessage -> {
                    addMessageUseCase(
                        prompt = event.prompt,
                        updateState = _uiState::update
                    )
                }
            }
        }
    }

    private fun loadMessages() {
        viewModelScope.launch {
            getMessagesUseCase(_uiState::update)
        }
    }
}