package com.ippon.geminitraveler.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ippon.geminitraveler.R
import com.ippon.geminitraveler.core.components.StateContainer
import com.ippon.geminitraveler.core.utils.State
import com.ippon.geminitraveler.domain.model.PlanTravel

@Composable
fun ChatbotScreen(
    uiState: State<PlanTravel> = State.Initial,
    requestPlanTravel: (String) -> Unit = {}
) {
    var prompt by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(all = 8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row {
            TextField(
                value = prompt,
                label = { Text(stringResource(R.string.summarize_label)) },
                placeholder = { Text(stringResource(R.string.summarize_hint)) },
                onValueChange = { prompt = it },
                modifier = Modifier
                    .weight(8f)
            )
            TextButton(
                onClick = {
                    if (prompt.isNotBlank()) {
                        requestPlanTravel(prompt)
                    }
                },

                modifier = Modifier
                    .weight(2f)
                    .padding(all = 4.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(stringResource(R.string.action_go))
            }
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
                Text(
                    text = it.data,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}