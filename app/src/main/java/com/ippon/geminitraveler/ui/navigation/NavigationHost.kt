package com.ippon.geminitraveler.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ippon.geminitraveler.ui.screens.ChatbotScreen
import com.ippon.geminitraveler.ui.screens.ContainerScreen
import com.ippon.geminitraveler.ui.screens.HomeScreen
import com.ippon.geminitraveler.ui.view_models.ChatHistoryViewModel
import com.ippon.geminitraveler.ui.view_models.ModelViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavigationHost() {
    val navController = rememberNavController()
    val chatHistoryViewModel: ChatHistoryViewModel = koinViewModel()
    val chatUiState by chatHistoryViewModel.uiState.collectAsState()

    ContainerScreen(
        chatHistoryItems = chatUiState.chats,
        onNavigate = {
            navController.navigate("${Destination.Chat.route}/$it")
        },
        onHandleEvent = chatHistoryViewModel::onHandleEvent,
    ) {
        NavHost(
            navController = navController,
            startDestination = Destination.Home.fullRoute
        ) {
            composable(
                route = Destination.Home.fullRoute,
                arguments = Destination.Home.arguments,
            ) {
                HomeScreen()
            }

            composable(
                route = Destination.Chat.fullRoute,
                arguments = Destination.Chat.arguments
            ) {
                val viewModel: ModelViewModel = koinViewModel()
                val uiState by viewModel.uiState.collectAsState()
                ChatbotScreen(
                    uiState = uiState,
                    onHandleEvent = viewModel::onHandleEvent
                )
            }
        }
    }

}