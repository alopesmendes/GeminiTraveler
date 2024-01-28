package com.ippon.geminitraveler.core.components

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ippon.geminitraveler.core.utils.Resource

@Composable
fun <T> StateContainer(
    modifier: Modifier,
    uiState: Resource<T>,
    initialComponent: @Composable () -> Unit,
    loadingComponent: @Composable () -> Unit,
    errorComponent: @Composable (Resource.Error) -> Unit,
    contentComponent: @Composable (T) -> Unit
) {
    Box(modifier = modifier) {
        when (uiState) {
            is Resource.Error -> errorComponent(uiState)
            Resource.Initial -> initialComponent()
            Resource.Loading -> loadingComponent()
            is Resource.Success -> contentComponent(uiState.data)
        }
    }
}