package com.ippon.geminitraveler.core.utils

sealed interface Resource<out T> {
    data class Error(
        val errorMessage: String?,
        val throwable: Throwable?,
    ): Resource<Nothing>

    data class Success<T>(
        val data: T
    ): Resource<T>
}
