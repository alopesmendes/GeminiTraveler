package com.ippon.geminitraveler

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.ippon.geminitraveler.ui.screens.ChatbotScreen
import com.ippon.geminitraveler.ui.theme.GeminiTravelerTheme
import com.ippon.geminitraveler.ui.view_models.ModelViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GeminiTravelerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
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
}