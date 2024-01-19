package com.ippon.geminitraveler.core.utils

sealed interface State<out T> {
    data object Loading: State<Nothing>
    data class Error(
        val errorMessage: String?,
        val throwable: Throwable?,
    ): State<Nothing>

    data class Success<T>(
        val data: T
    ): State<T>
}
