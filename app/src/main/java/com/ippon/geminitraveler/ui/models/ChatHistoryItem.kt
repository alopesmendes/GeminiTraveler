package com.ippon.geminitraveler.ui.models

import androidx.compose.runtime.Immutable

@Immutable
data class ChatHistoryItem(
    val id: Int,
    val title: String,
    val date: String
)
