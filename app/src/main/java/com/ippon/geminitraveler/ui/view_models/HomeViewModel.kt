package com.ippon.geminitraveler.ui.view_models

import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ippon.geminitraveler.domain.use_cases.GetDescriptionUseCase
import com.ippon.geminitraveler.ui.models.HomeUiState
import com.ippon.geminitraveler.ui.models.events.HomeEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(
    private val getDescriptionUseCase: GetDescriptionUseCase,
): ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        onHandleEvent(HomeEvent.GetGeminiDescription)
    }

    companion object {
        private val PROMPT = """
            Can you give the description of the new AI model Gemini 
            made by Google in the following language: ${Locale.current.language} 
        """
    }

    fun onHandleEvent(homeEvent: HomeEvent) {
        viewModelScope.launch {
            when (homeEvent) {
                HomeEvent.GetGeminiDescription -> {
                    getDescriptionUseCase(
                        updateState = _uiState::update,
                        prompt = PROMPT
                    )
                }

                HomeEvent.GetNextPartDescription -> {
                    _uiState.update { state ->
                        val index = if (state.descriptionIndex < state.descriptions.lastIndex) {
                            state.descriptionIndex + 1
                        } else {
                            state.descriptionIndex
                        }
                        state.copy(
                            descriptionIndex = index
                        )
                    }
                }
            }
        }
    }

}