package com.ippon.geminitraveler.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ippon.geminitraveler.R
import com.ippon.geminitraveler.ui.components.ChatHistoryList
import com.ippon.geminitraveler.ui.components.CustomTopBar
import com.ippon.geminitraveler.ui.models.ChatEvent
import com.ippon.geminitraveler.ui.models.ChatHistoryItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContainerScreen(
    chatHistoryItems: List<ChatHistoryItem>,
    onHandleEvent: (ChatEvent) -> Unit,
    onNavigate: (Long) -> Unit,
    content: @Composable () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        gesturesEnabled = true,
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ChatHistoryList(
                        modifier = Modifier
                            .weight(1F)
                            .padding(bottom = 8.dp),
                        chatHistoryItems = chatHistoryItems,
                        scope = scope,
                        drawerState = drawerState,
                        onNavigate = onNavigate
                    )

                    ExtendedFloatingActionButton(
                        text = { Text(text = stringResource(id = R.string.btn_new_chat)) },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = ""
                            )
                        },
                        onClick = {
                            onHandleEvent(ChatEvent.CreateNewChat)
                            scope.launch {
                                drawerState.close()
                            }
                        },
                        shape = FloatingActionButtonDefaults.largeShape,
                        modifier = Modifier.padding(16.dp),
                    )
                }
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                CustomTopBar(
                    scope = scope,
                    drawerState = drawerState,
                    scrollBehavior = scrollBehavior
                )
            },
        ) { innerPadding ->
            Surface(modifier = Modifier.padding(innerPadding)) {
                content()
            }
        }
    }
}

@Preview
@Composable
fun ContainerScreenPreview() {
    ContainerScreen(
        chatHistoryItems = (1..20).map { ChatHistoryItem(id = it.toLong(), title = "test", "01/01/2023") },
        onNavigate = { },
        onHandleEvent = { },
    ) {
        Text(text = "Je suis un chatbot")
    }
}