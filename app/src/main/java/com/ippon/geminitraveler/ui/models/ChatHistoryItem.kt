package com.ippon.geminitraveler.ui.models

import androidx.compose.runtime.Immutable

@Immutable
data class ChatHistoryItem(
    val id: Long,
    val title: String,
    val createAt: String
)
