package com.ippon.geminitraveler.domain.use_cases

import com.ippon.geminitraveler.core.utils.DataState
import com.ippon.geminitraveler.core.utils.Resource
import com.ippon.geminitraveler.domain.model.ModelRequest
import com.ippon.geminitraveler.domain.repository.MessagesRepository
import com.ippon.geminitraveler.ui.mapper.mapToPlanTravelUi
import com.ippon.geminitraveler.ui.models.ModelResponseUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class GetModelResponseUseCase(
    private val planTravelRepository: MessagesRepository
) {
    operator fun invoke(
        prompt: String,
        initialUiState: ModelResponseUiState,
    ): Flow<ModelResponseUiState> {
        var uiState = initialUiState
        val requestPlan = ModelRequest(prompt)

        return planTravelRepository
            .getMessages(requestPlan)
            .map { resource ->
                uiState = when (resource) {
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
                            planTravels = listOf(
                                *uiState.planTravels.toTypedArray(),
                                resource.data.mapToPlanTravelUi(),
                            )
                        )
                    }
                }
                uiState
            }
    }
}