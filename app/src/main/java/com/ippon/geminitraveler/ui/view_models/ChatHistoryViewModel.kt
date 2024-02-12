package com.ippon.geminitraveler.ui.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ippon.geminitraveler.domain.use_cases.AddChatUseCase
import com.ippon.geminitraveler.domain.use_cases.DeleteChatUseCase
import com.ippon.geminitraveler.domain.use_cases.GetChatsUseCase
import com.ippon.geminitraveler.domain.use_cases.UpdateChatTitleUseCase
import com.ippon.geminitraveler.ui.models.ChatEvent
import com.ippon.geminitraveler.ui.models.ChatUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ChatHistoryViewModel(
    private val getChatsUseCase: GetChatsUseCase,
    private val addChatUseCase: AddChatUseCase,
    private val deleteChatUseCase: DeleteChatUseCase,
    private val updateChatTitleUseCase: UpdateChatTitleUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadChats()
    }
    fun onHandleEvent(chatEvent: ChatEvent) {
        viewModelScope.launch {
            when (chatEvent) {
                is ChatEvent.SelectChat -> {
                    _uiState.update { state ->
                        state.copy(
                            currentChatId = chatEvent.chatId
                        )
                    }
                }

                ChatEvent.CreateNewChat -> {
                    addChatUseCase(
                        title = "Default title",
                        updateState = _uiState::update
                    )
                }

                is ChatEvent.ChangeChatTitle -> {
                    updateChatTitleUseCase(
                        chatId = chatEvent.chatId,
                        title = chatEvent.title,
                        updateState = _uiState::update
                    )
                }
                is ChatEvent.DeleteChat -> {
                    deleteChatUseCase(
                        chatId = chatEvent.chatId,
                        updateState = _uiState::update
                    )
                }
            }
        }
    }

    private fun loadChats() {
        viewModelScope.launch {
            getChatsUseCase(_uiState::update)
        }
    }
}