package com.ippon.geminitraveler.ui.models

import androidx.compose.runtime.Immutable
import com.ippon.geminitraveler.core.utils.DataState
import com.ippon.geminitraveler.core.utils.UiState

@Immutable
data class MessagesUiState(
    override val dataState: DataState = DataState.INITIAL,
    val messages: List<MessageUi> = emptyList(),
    val errorMessage: String? = null,
    val currentMessageId: Long? = null,
): UiState
