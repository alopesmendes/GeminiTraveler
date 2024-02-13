package com.ippon.geminitraveler.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp

private const val IndicatorSize = 16
private const val AnimationDurationMillis = 300
private const val NumIndicators = 3
private const val AnimationDelayMillis = AnimationDurationMillis / NumIndicators

@Composable
private fun LoadingDot(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(color)
    )
}

@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface
) {
    val infiniteTransition = rememberInfiniteTransition(label = "circle loading")
    val animatedValues = List(NumIndicators) { index: Int ->
        val animatedValue by infiniteTransition.animateFloat(
            initialValue = IndicatorSize / 2f,
            targetValue = -IndicatorSize / 2f,
            animationSpec = infiniteRepeatable(
                animation = tween(AnimationDurationMillis),
                repeatMode = RepeatMode.Reverse,
                initialStartOffset = StartOffset(AnimationDelayMillis * index)
            ),
            label = "animated dot $index"
        )
        animatedValue
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        animatedValues.forEach { animatedValue ->
            LoadingDot(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .size(IndicatorSize.dp)
                    .offset(y = animatedValue.dp),
                color = color
            )
        }
    }

}

@Preview
@PreviewLightDark
@Composable
private fun PreviewLoadingIndicator() {
    LoadingIndicator(
        color = MaterialTheme.colorScheme.primary
    )
}