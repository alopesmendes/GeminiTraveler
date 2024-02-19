package com.ippon.geminitraveler.core.utils

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode

object Tools {
    fun createGradientBrush(
        offset: Float,
        gradientColors: List<Color> = Constants.GRADIENT_COLORS
    ) = object : ShaderBrush() {
        override fun createShader(size: Size): Shader {
            val widthOffset = size.width * offset
            val heightOffset = size.height * offset
            return LinearGradientShader(
                colors = gradientColors,
                from = Offset(widthOffset, heightOffset),
                to = Offset(widthOffset + size.width, heightOffset + size.height),
                tileMode = TileMode.Mirror
            )
        }
    }

    private fun LazyListState.isScrolledToTheEnd() : Boolean {
        val lastItem = layoutInfo.visibleItemsInfo.lastOrNull()
        return lastItem == null || lastItem.size + lastItem.offset <= layoutInfo.viewportEndOffset
    }

    suspend fun LazyListState.scrollToEnd() {
        if (!isScrolledToTheEnd()) {
            val itmIndex = layoutInfo.totalItemsCount - 1
            if (itmIndex >= 0) {
                val lastItem = layoutInfo.visibleItemsInfo.lastOrNull()
                lastItem?.let {
                    animateScrollToItem(itmIndex, it.size + it.offset)
                }
            }
        }
    }
}