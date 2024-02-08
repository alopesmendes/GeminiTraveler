package com.ippon.geminitraveler.ui.models

import androidx.compose.runtime.Immutable
import com.ippon.geminitraveler.core.utils.DataState
import com.ippon.geminitraveler.core.utils.UiState

@Immutable
data class ChatUiState(
    override val dataState: DataState = DataState.INITIAL,
    val chats: List<ChatHistoryItem> = emptyList(),
    val errorMessage: String? = null,
    val currentChatId: Long? = null,
): UiState
