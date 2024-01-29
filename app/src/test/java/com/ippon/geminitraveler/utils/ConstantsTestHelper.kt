package com.ippon.geminitraveler.utils

import com.ippon.geminitraveler.core.utils.DataState
import com.ippon.geminitraveler.core.utils.Resource
import com.ippon.geminitraveler.data.mappers.mapToPlanTravel
import com.ippon.geminitraveler.domain.model.PlanTravel
import com.ippon.geminitraveler.domain.model.RequestPlan
import com.ippon.geminitraveler.domain.model.Role
import com.ippon.geminitraveler.ui.mapper.mapToPlanTravelUi
import com.ippon.geminitraveler.ui.models.PlanTravelEvent
import com.ippon.geminitraveler.ui.models.PlanTravelUiState

object ConstantsTestHelper {
    const val MODEL_RESPONSE = "response"
    const val REQUEST_PLAN_DATA = "request"

    val planTravelModel = PlanTravel(
        data = MODEL_RESPONSE,
        role = Role.MODEL
    )

    val requestPlan = RequestPlan(REQUEST_PLAN_DATA)

    val userResource: Resource<PlanTravel> = Resource.Success(
        requestPlan.mapToPlanTravel()
    )

    val modelResource: Resource<PlanTravel> = Resource.Success(
        planTravelModel
    )

    private const val ERROR_MESSAGE = "error"
    private val throwable = IllegalStateException(ERROR_MESSAGE)

    val errorResource: Resource<PlanTravel> = Resource.Error(
        errorMessage = ERROR_MESSAGE,
        throwable = throwable
    )

    private val planTravels = listOf(planTravelModel)

    val uiPlanTravels = planTravels.map { it.mapToPlanTravelUi() }

    val resourcePlanTravels = planTravels.map {
        Resource.Success(it)
    }

    val initialPlanTravelUiState = PlanTravelUiState()
    val successPlanTravelUiState = PlanTravelUiState(
        dataState = DataState.SUCCESS,
        planTravels = uiPlanTravels
    )
    val errorPlanTravelUiState = PlanTravelUiState(
        dataState = DataState.ERROR,
        errorMessage = ERROR_MESSAGE
    )

    val requestModelEvent = PlanTravelEvent.ModelRequestEvent(
        prompt = REQUEST_PLAN_DATA
    )
}