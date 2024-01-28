package com.ippon.geminitraveler.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ippon.geminitraveler.core.components.StateContainer
import com.ippon.geminitraveler.core.utils.Resource
import com.ippon.geminitraveler.domain.model.PlanTravel
import com.ippon.geminitraveler.ui.components.ChatRow
import com.ippon.geminitraveler.ui.components.CustomTextField
import com.ippon.geminitraveler.ui.components.SendButton

@Composable
fun ChatbotScreen(
    uiState: Resource<PlanTravel> = Resource.Initial,
    requestPlanTravel: (String) -> Unit = {}
) {
    var prompt by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        StateContainer(
            modifier = Modifier.weight(1f),
            uiState = uiState,
            initialComponent = {  },
            loadingComponent = { LoadingChatbotScreenComponents() },
            errorComponent = {
                ErrorChatbotScreenComponents(it)
            }
        ) {
            ChatRow(speechContent = it.data, isGemini = true)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            CustomTextField(
                prompt = prompt,
                onPromptChange = { prompt = it },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            SendButton(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f),
                sendMessage = {
                    if (prompt.isNotBlank()) {
                        requestPlanTravel(prompt)
                    }
                }
            )
        }
    }
}

@Composable
private fun ColumnScope.LoadingChatbotScreenComponents() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(all = 8.dp)
            .align(Alignment.CenterHorizontally)
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorChatbotScreenComponents(stateError: Resource.Error) {
    Text(
        text = stateError.errorMessage ?: "",
        color = Color.Red,
        modifier = Modifier.padding(all = 8.dp)
    )
}

@Composable
@Preview
fun ChatbotScreenPreview() {
    ChatbotScreen()
}