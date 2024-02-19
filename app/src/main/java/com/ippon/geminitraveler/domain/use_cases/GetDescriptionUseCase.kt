package com.ippon.geminitraveler.domain.use_cases

import com.ippon.geminitraveler.core.utils.DataState
import com.ippon.geminitraveler.domain.datasources.GenerativeDataSource
import com.ippon.geminitraveler.ui.models.HomeUiState
import org.koin.core.annotation.Single

@Single
class GetDescriptionUseCase(
    private val generativeDataSource: GenerativeDataSource
) {

    suspend operator fun invoke(
        prompt: String,
        updateState: ((HomeUiState) -> HomeUiState) -> Unit,
    ) {
        try {
            updateState.invoke { state -> state.copy(dataState = DataState.LOADING) }
            val content = generativeDataSource.generateContentStream(prompt)
            content.collect {
                updateState.invoke { state ->
                    state.copy(
                        dataState = DataState.SUCCESS,
                        descriptions = listOf(*state.descriptions.toTypedArray(), it),
                    )
                }
            }
        } catch (e: Exception) {
            updateState.invoke { state ->
                state.copy(
                    dataState = DataState.ERROR,
                    errorMessage = e.message,
                )
            }
        }

    }
}