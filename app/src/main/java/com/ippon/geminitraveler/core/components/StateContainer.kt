package com.ippon.geminitraveler.core.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ippon.geminitraveler.core.utils.DataState
import com.ippon.geminitraveler.core.utils.UiState

@Composable
fun StateContainer(
    modifier: Modifier,
    uiState: UiState,
    initialComponent: @Composable () -> Unit,
    loadingComponent: @Composable () -> Unit,
    errorComponent: @Composable () -> Unit,
    contentComponent: @Composable () -> Unit
) {
    AnimatedContent(
        targetState = uiState.dataState,
        label = "Animate different data states",
        modifier = modifier,
    ) { targetState: DataState ->
        when (targetState) {
            DataState.INITIAL -> initialComponent()
            DataState.LOADING -> loadingComponent()
            DataState.ERROR -> errorComponent()
            DataState.SUCCESS -> contentComponent()
        }
    }
}