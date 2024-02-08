package com.ippon.geminitraveler.ui.screens

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes

@Composable
fun HomeScreen() {
    Text(text = "chat bot screen")
}

@Preview
@PreviewScreenSizes
@PreviewFontScale
@PreviewLightDark
@PreviewDynamicColors
@Composable
private fun HomeScreenPreview() {
    HomeScreen()
}