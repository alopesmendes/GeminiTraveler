package com.ippon.geminitraveler.domain.use_cases

import com.ippon.geminitraveler.core.utils.DataState
import com.ippon.geminitraveler.core.utils.Resource
import com.ippon.geminitraveler.domain.model.RequestPlan
import com.ippon.geminitraveler.domain.repository.PlanTravelRepository
import com.ippon.geminitraveler.ui.models.PlanTravelUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class GetPlanTravelUseCase(
    private val planTravelRepository: PlanTravelRepository
) {
    operator fun invoke(
        prompt: String,
        initialUiState: PlanTravelUiState,
    ): Flow<PlanTravelUiState> {
        var uiState = initialUiState
        val requestPlan = RequestPlan(prompt)

        return planTravelRepository
            .getPlanTravel(requestPlan)
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
                                resource.data
                            )
                        )
                    }
                }
                uiState
            }
    }
}