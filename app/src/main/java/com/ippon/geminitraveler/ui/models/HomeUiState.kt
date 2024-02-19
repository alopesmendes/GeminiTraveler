package com.ippon.geminitraveler.ui.models

import androidx.compose.runtime.Immutable
import com.ippon.geminitraveler.core.utils.DataState
import com.ippon.geminitraveler.core.utils.UiState

@Immutable
data class HomeUiState(
    override val dataState: DataState = DataState.INITIAL,
    val errorMessage: String? = null,
    val descriptions: List<String> = emptyList(),
    val descriptionIndex: Int = 0,
): UiState
