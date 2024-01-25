package com.ippon.geminitraveler.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ippon.geminitraveler.R

@Composable
fun ChatRow(speechContent: String, isGemini: Boolean) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .verticalScroll(rememberScrollState()),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    )
    {
        var backgroundColor = MaterialTheme.colorScheme.primary
        if (isGemini) {
            backgroundColor = MaterialTheme.colorScheme.tertiary
            Image(
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(32.dp),
                painter = painterResource(id = R.drawable.ic_robot),
                contentDescription = "AI icon"
            )
        }
        SpeechBubble(color = backgroundColor, content = speechContent, isGemini = isGemini)
    }
}

@Composable
@Preview(apiLevel = 33)
fun ChatRowPreview() {
    Column {
        ChatRow(speechContent = "Hello, I'm Gemini", isGemini = true)
        ChatRow(speechContent = "Plan me a vacation", isGemini = false)
    }
}