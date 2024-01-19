package com.ippon.geminitraveler.domain.use_cases

import com.ippon.geminitraveler.core.utils.State
import com.ippon.geminitraveler.domain.model.PlanTravel
import com.ippon.geminitraveler.domain.model.RequestPlan
import com.ippon.geminitraveler.domain.repository.PlanTravelRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class GetPlanTravelUseCase(
    private val planTravelRepository: PlanTravelRepository
) {
    operator fun invoke(requestPlan: RequestPlan): Flow<State<PlanTravel>> = flow {
        emit(State.Loading)

        try {
            val response = planTravelRepository.getPlanTravel(requestPlan)
            emit(
                State.Success(
                    data = response
                )
            )
        } catch (e: Exception) {
            emit(
                State.Error(
                    throwable = e,
                    errorMessage = e.message
                )
            )
        }
    }
}