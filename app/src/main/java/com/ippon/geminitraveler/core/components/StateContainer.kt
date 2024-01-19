package com.ippon.geminitraveler.core.components

import androidx.compose.runtime.Composable
import com.ippon.geminitraveler.core.utils.State

@Composable
fun <T> StateContainer(
    uiState: State<T>,
    initialComponent: @Composable () -> Unit,
    loadingComponent: @Composable () -> Unit,
    errorComponent: @Composable (State.Error) -> Unit,
    contentComponent: @Composable (T) -> Unit
) {
    when (uiState) {
        is State.Error -> errorComponent(uiState)
        State.Initial -> initialComponent()
        State.Loading -> loadingComponent()
        is State.Success -> contentComponent(uiState.data)
    }
}