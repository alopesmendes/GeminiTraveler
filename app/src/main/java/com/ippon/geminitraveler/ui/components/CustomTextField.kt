package com.ippon.geminitraveler.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ippon.geminitraveler.R
import com.ippon.geminitraveler.ui.theme.md_theme_light_onTertiary
import com.ippon.geminitraveler.ui.theme.md_theme_light_tertiary

@Composable
fun CustomTextField(prompt: String, onPromptChange: (String) -> Unit, modifier: Modifier) {
    TextField(
        value = prompt,
        placeholder = { Text(stringResource(R.string.summarize_hint)) },
        onValueChange = { onPromptChange(it) },
        shape = RoundedCornerShape(100),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = md_theme_light_tertiary,
            focusedContainerColor = md_theme_light_tertiary,
            focusedTextColor = md_theme_light_onTertiary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            ),
        modifier = modifier
    )
}

@Composable
@Preview
fun CustomTextFieldPreview() {
    CustomTextField(prompt = "", onPromptChange = { }, modifier = Modifier)
}