package com.ippon.geminitraveler.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import com.ippon.geminitraveler.core.components.MarkdownText
import com.ippon.geminitraveler.core.components.StateContainer
import com.ippon.geminitraveler.core.utils.State
import com.ippon.geminitraveler.domain.model.PlanTravel
import com.ippon.geminitraveler.ui.components.CustomTextField
import com.ippon.geminitraveler.ui.components.SendButton

@Composable
fun ChatbotScreen(
    uiState: State<PlanTravel> = State.Initial,
    requestPlanTravel: (String) -> Unit = {}
) {
    var prompt by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            CustomTextField(prompt = prompt, onPromptChange = { prompt = it })
            Spacer(modifier = Modifier.width(8.dp))
            SendButton(
                sendMessage = {
                    if (prompt.isNotBlank()) {
                        requestPlanTravel(prompt)
                    }
                }
            )
        }
        StateContainer(
            uiState = uiState,
            initialComponent = { /*TODO*/ },
            loadingComponent = {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(all = 8.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    CircularProgressIndicator()
                }
            },
            errorComponent = {
                Text(
                    text = it.errorMessage ?: "",
                    color = Color.Red,
                    modifier = Modifier.padding(all = 8.dp)
                )
            }
        ) {
            Row(modifier = Modifier.padding(all = 8.dp)) {
                Icon(
                    Icons.Outlined.Person,
                    contentDescription = "Person Icon"
                )
                MarkdownText(
                    markdownContent = it.data
                )
            }
        }
    }
}

@Composable
@Preview
fun ChatbotScreenPreview() {
    ChatbotScreen()
}