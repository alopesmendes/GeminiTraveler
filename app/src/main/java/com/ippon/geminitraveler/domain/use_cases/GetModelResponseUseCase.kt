package com.ippon.geminitraveler.domain.use_cases

import com.ippon.geminitraveler.core.utils.DataState
import com.ippon.geminitraveler.core.utils.Resource
import com.ippon.geminitraveler.domain.repository.MessagesRepository
import com.ippon.geminitraveler.ui.mapper.mapToModelResponseUi
import com.ippon.geminitraveler.ui.models.ModelResponseUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class GetModelResponseUseCase(
    private val planTravelRepository: MessagesRepository
) {
    operator fun invoke(
        uiState: ModelResponseUiState,
    ): Flow<ModelResponseUiState> {

        return planTravelRepository
            .getMessages()
            .map { resource ->
                when (resource) {
                    is Resource.Error -> {
                        uiState.copy(
                            dataState = DataState.ERROR,
                            errorMessage = resource.errorMessage,
                        )
                    }
                    Resource.Loading -> {
                        uiState.copy(
                            dataState = DataState.LOADING,
                        )
                    }
                    is Resource.Success -> {
                        uiState.copy(
                            dataState = DataState.SUCCESS,
                            planTravels = resource.data.map { it.mapToModelResponseUi() }
                        )
                    }
                }
            }
    }
}