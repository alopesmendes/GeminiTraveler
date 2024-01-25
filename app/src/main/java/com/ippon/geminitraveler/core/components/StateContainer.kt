package com.ippon.geminitraveler.core.components

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ippon.geminitraveler.core.utils.State

@Composable
fun <T> StateContainer(
    modifier: Modifier,
    uiState: State<T>,
    initialComponent: @Composable () -> Unit,
    loadingComponent: @Composable () -> Unit,
    errorComponent: @Composable (State.Error) -> Unit,
    contentComponent: @Composable (T) -> Unit
) {
    Box(modifier = modifier) {
        when (uiState) {
            is State.Error -> errorComponent(uiState)
            State.Initial -> initialComponent()
            State.Loading -> loadingComponent()
            is State.Success -> contentComponent(uiState.data)
        }
    }
}