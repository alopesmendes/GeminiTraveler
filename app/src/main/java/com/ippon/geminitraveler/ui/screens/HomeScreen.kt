package com.ippon.geminitraveler.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.ippon.geminitraveler.R
import com.ippon.geminitraveler.core.components.MarkdownText
import com.ippon.geminitraveler.core.components.StateContainer
import com.ippon.geminitraveler.core.utils.DataState
import com.ippon.geminitraveler.core.utils.Tools
import com.ippon.geminitraveler.ui.components.LoadingIndicator
import com.ippon.geminitraveler.ui.models.HomeUiState
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "Text title animation")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset for chat bot image"
    )

    val brush = remember(offset) {
        Tools.createGradientBrush(offset)
    }

    StateContainer(
        modifier = modifier,
        uiState = uiState,
        initialComponent = {  },
        loadingComponent = {
            LoadingHomeScreen()
        },
        errorComponent = {
            ErrorHomeScreen(
                errorMessage = uiState.errorMessage ?: ""
            )
        }
    ) {
        SuccessHomeScreen(
            brush = brush,
            description = uiState.description
        )
    }

}

@Composable
private fun SuccessHomeScreen(
    modifier: Modifier = Modifier,
    brush: Brush,
    description: String,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Image(
            painterResource(id = R.drawable.ic_banner_ai),
            contentDescription = "banner",
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    border = BorderStroke(
                        width = 4.dp,
                        brush = brush,
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                .background(
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    shape = RoundedCornerShape(12.dp)
                ),
            contentScale = ContentScale.Crop,
        )

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 8.dp)
        ) {
            MarkdownText(
                markdownContent = description
            )
        }
    }
}

@Composable
private fun LoadingHomeScreen(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LoadingIndicator(
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun ErrorHomeScreen(
    modifier: Modifier = Modifier,
    errorMessage: String
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.headlineSmall,
        )
    }
}

@Preview(apiLevel = 33)
@PreviewScreenSizes
@PreviewFontScale
@PreviewLightDark
@PreviewDynamicColors
@Composable
private fun HomeScreenPreview() {
    var homeUiState by remember {
        mutableStateOf(HomeUiState(dataState = DataState.LOADING))
    }
    LaunchedEffect(Unit) {
        delay(5.seconds)
        homeUiState = homeUiState.copy(
            dataState = DataState.SUCCESS,
            description = "Test description"
        )
    }
    HomeScreen(
        uiState = homeUiState
    )
}