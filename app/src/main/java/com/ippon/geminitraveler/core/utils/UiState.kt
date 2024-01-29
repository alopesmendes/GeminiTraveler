package com.ippon.geminitraveler.core.utils

interface UiState {
    val dataState: DataState
}

enum class DataState {
    INITIAL, LOADING, ERROR, SUCCESS
}