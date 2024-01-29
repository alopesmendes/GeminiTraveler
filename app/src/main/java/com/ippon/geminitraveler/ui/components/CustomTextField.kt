package com.ippon.geminitraveler.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ippon.geminitraveler.R

@Composable
fun CustomTextField(prompt: String, onPromptChange: (String) -> Unit, modifier: Modifier) {
    TextField(
        value = prompt,
        placeholder = { Text(stringResource(R.string.summarize_hint)) },
        onValueChange = { onPromptChange(it) },
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.colors(
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