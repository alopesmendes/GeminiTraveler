package com.ippon.geminitraveler.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ippon.geminitraveler.ui.components.ChatList
import com.ippon.geminitraveler.ui.components.CustomTextField
import com.ippon.geminitraveler.ui.components.SendButton
import com.ippon.geminitraveler.ui.models.PlanTravelEvent
import com.ippon.geminitraveler.ui.models.PlanTravelUiState

@Composable
fun ChatbotScreen(
    uiState: PlanTravelUiState = PlanTravelUiState(),
    onHandleEvent: (PlanTravelEvent) -> Unit = {}
) {
    var prompt by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 8.dp)
    ) {
        ChatList(
            modifier = Modifier.weight(1f),
            plantTravelUiState = uiState
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ) {
            CustomTextField(
                prompt = prompt,
                onPromptChange = { prompt = it },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            SendButton(
                modifier = Modifier,
                sendMessage = {
                    if (prompt.isNotBlank()) {
                        onHandleEvent(
                            PlanTravelEvent.ModelRequestEvent(prompt)
                        )
                    }
                }
            )
        }
    }
}

@Composable
private fun SuccessChatbotScreenComponent(
    modifier: Modifier = Modifier,
    planTravelUiState: PlanTravelUiState,
) {
    ChatList(
        modifier = modifier,
        plantTravelUiState = planTravelUiState,
    )
}

@Composable
@Preview
fun ChatbotScreenPreview() {
    ChatbotScreen()
}