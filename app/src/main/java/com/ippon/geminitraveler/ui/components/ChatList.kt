package com.ippon.geminitraveler.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.ippon.geminitraveler.core.components.StateContainer
import com.ippon.geminitraveler.core.utils.DataState
import com.ippon.geminitraveler.core.utils.Tools.scrollToEnd
import com.ippon.geminitraveler.ui.models.MessageUi
import com.ippon.geminitraveler.ui.models.MessagesUiState
import com.ippon.geminitraveler.ui.models.RoleUi

@Composable
fun ChatList(
    modifier: Modifier = Modifier,
    messagesUiState: MessagesUiState,
) {
    val messages by remember(messagesUiState.messages.size) {
        derivedStateOf { messagesUiState.messages }
    }
    val state = rememberLazyListState()

    LazyColumn(
        modifier = modifier,
        state = state
    ) {
        items(messages.size) { index ->
            ChatRow(
                speechContent = messages[index].data,
                isGemini = messages[index].role == RoleUi.MODEL,
            )
        }
        item {
            StateContainer(
                modifier = Modifier.fillMaxWidth(),
                uiState = messagesUiState,
                initialComponent = {  },
                loadingComponent = {
                    LoadingChatBotScreenComponents()
                },
                errorComponent = {
                    ErrorChatBotScreenComponents(
                        errorMessage = messagesUiState.errorMessage
                    )
                },
                contentComponent = {
                    LaunchedEffect(Unit) {
                        state.animateScrollToItem(
                            index = messages.lastIndex
                        )
                    }
                }
            )
        }
    }

    LaunchedEffect(state.canScrollForward, messages.size) {
        state.scrollToEnd()
    }
}

@Composable
private fun ErrorChatBotScreenComponents(
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
private fun LoadingChatBotScreenComponents() {
    ChatRow(isGemini = true) {
        LoadingIndicator(
            modifier = Modifier
                .padding(4.dp),
            color = MaterialTheme.colorScheme.onTertiary
        )
    }
}

@Composable
@Preview
@PreviewLightDark
private fun LoadingChatBotScreenComponentsPreview() {
    LoadingChatBotScreenComponents()
}

@Preview(name = "ChatList")
@Composable
private fun PreviewChatList() {
    ChatList(
        messagesUiState = MessagesUiState(
            dataState = DataState.SUCCESS,
            messages = (1..10).map { index ->
                MessageUi(
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