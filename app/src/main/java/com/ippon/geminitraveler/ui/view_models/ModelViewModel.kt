package com.ippon.geminitraveler.ui.view_models

 import androidx.lifecycle.SavedStateHandle
 import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ippon.geminitraveler.domain.use_cases.AddMessageUseCase
import com.ippon.geminitraveler.domain.use_cases.GetMessagesFromChatUseCase
import com.ippon.geminitraveler.ui.models.MessagesUiState
import com.ippon.geminitraveler.ui.models.events.ModelEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ModelViewModel(
    private val addMessageUseCase: AddMessageUseCase,
    private val getMessagesFromChatUseCase: GetMessagesFromChatUseCase,
    savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val chatId: Long? = savedStateHandle["id"]
    private val _uiState: MutableStateFlow<MessagesUiState> = MutableStateFlow(MessagesUiState())
    val uiState = _uiState.asStateFlow()

    init {
        chatId?.let { id ->
            onHandleEvent(ModelEvent.GetMessages(id))
        }
    }

    fun onHandleEvent(event: ModelEvent) {
        viewModelScope.launch {
            when (event) {
                is ModelEvent.UserSendMessage -> {
                    chatId?.let { id ->
                        addMessageUseCase(
                            prompt = event.prompt,
                            updateState = _uiState::update,
                            chatId = id
                        )
                    }
                }

                is ModelEvent.GetMessages -> {
                    getMessagesFromChatUseCase(
                        chatId = event.chatId,
                        updateState = _uiState::update
                    )
                }
            }
        }
    }

}