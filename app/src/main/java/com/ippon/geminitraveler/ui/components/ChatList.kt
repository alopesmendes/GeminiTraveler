package com.ippon.geminitraveler.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ippon.geminitraveler.core.components.StateContainer
import com.ippon.geminitraveler.core.utils.DataState
import com.ippon.geminitraveler.ui.models.ModelResponseUi
import com.ippon.geminitraveler.ui.models.ModelResponseUiState
import com.ippon.geminitraveler.ui.models.RoleUi

@Composable
fun ChatList(
    modifier: Modifier = Modifier,
    plantTravelUiState: ModelResponseUiState,
) {
    val visibleIndex by remember(plantTravelUiState.planTravels.lastIndex) {
        derivedStateOf {
            if (plantTravelUiState.planTravels.lastIndex == -1) {
                0
            } else {
                plantTravelUiState.planTravels.lastIndex
            }
        }
    }
    val state = rememberLazyListState(
        initialFirstVisibleItemIndex = visibleIndex
    )

    LazyColumn(
        modifier = modifier,
        state = state
    ) {
        items(plantTravelUiState.planTravels) { planTravel ->
            ChatRow(
                speechContent = planTravel.data,
                isGemini = planTravel.role == RoleUi.MODEL,
            )
        }
        item {
            StateContainer(
                modifier = Modifier.fillMaxWidth(),
                uiState = plantTravelUiState,
                initialComponent = {  },
                loadingComponent = {
                    LoadingChatbotScreenComponents(
                        modifier = Modifier.fillMaxWidth()
                    )
                                   },
                errorComponent = {
                    ErrorChatbotScreenComponents(
                        errorMessage = plantTravelUiState.errorMessage
                    )
                },
                contentComponent = { }
            )
        }
    }

    LaunchedEffect(visibleIndex) {
        state.animateScrollToItem(
            index = visibleIndex,
        )
    }
}

@Composable
private fun ErrorChatbotScreenComponents(
    modifier: Modifier = Modifier,
    errorMessage: String?
) {
    Text(
        text = errorMessage ?: "",
        color = Color.Red,
        modifier = modifier.padding(all = 8.dp)
    )
}

@Composable
private fun LoadingChatbotScreenComponents(
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 8.dp)
    ) {
        CircularProgressIndicator()
    }
}

@Preview(name = "ChatList")
@Composable
private fun PreviewChatList() {
    ChatList(
        plantTravelUiState = ModelResponseUiState(
            dataState = DataState.SUCCESS,
            planTravels = (1..10).map { index ->
                ModelResponseUi(
                    data = "text $index",
                    role = if (index % 2 == 0) {
                        RoleUi.USER
                    } else {
                        RoleUi.MODEL
                    }
                )
            }
        )
    )
}