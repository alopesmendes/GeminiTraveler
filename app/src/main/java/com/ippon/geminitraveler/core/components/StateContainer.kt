package com.ippon.geminitraveler.core.components

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ippon.geminitraveler.core.utils.DataState
import com.ippon.geminitraveler.core.utils.Resource
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
    Box(modifier = modifier) {
        when (uiState.dataState) {
            DataState.INITIAL -> initialComponent()
            DataState.LOADING -> loadingComponent()
            DataState.ERROR -> errorComponent()
            DataState.SUCCESS -> contentComponent()
        }
    }
}