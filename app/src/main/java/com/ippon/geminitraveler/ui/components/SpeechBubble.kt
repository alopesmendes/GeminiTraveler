package com.ippon.geminitraveler.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ippon.geminitraveler.core.components.MarkdownText
import com.ippon.geminitraveler.ui.theme.md_theme_light_primary

@Composable
fun SpeechBubble(color: Color, content: String, isGemini: Boolean) {
    Row {
        val cardShape: RoundedCornerShape by remember(isGemini) {
            derivedStateOf {
                if (isGemini) RoundedCornerShape(0.dp, 8.dp, 8.dp, 8.dp)
                else RoundedCornerShape(8.dp, 0.dp, 8.dp, 8.dp)
            }
        }
        ElevatedCard(
            shape = cardShape,
            colors = CardDefaults.cardColors(
                containerColor = color,
                contentColor = MaterialTheme.colorScheme.onTertiary
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                MarkdownText(markdownContent = content)
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SpeechBubblePreview() {
    SpeechBubble(color = md_theme_light_primary, content = "Hello I'm Gemini", isGemini = true)
}