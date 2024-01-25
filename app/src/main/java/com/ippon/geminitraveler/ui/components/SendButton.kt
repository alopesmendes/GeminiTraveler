package com.ippon.geminitraveler.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ippon.geminitraveler.ui.theme.md_theme_light_onPrimary
import com.ippon.geminitraveler.ui.theme.md_theme_light_primary

@Composable
fun SendButton(sendMessage: () -> Unit, modifier: Modifier) {
    FilledIconButton(
        onClick = { sendMessage() },
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = md_theme_light_primary,
            contentColor = md_theme_light_onPrimary,
        ),
        modifier = modifier
    ) {
        Icon(imageVector = Icons.Filled.Send, contentDescription = "Send message")
    }
}