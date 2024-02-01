package com.ippon.geminitraveler.utils

import com.ippon.geminitraveler.core.utils.DataState
import com.ippon.geminitraveler.core.utils.Resource
import com.ippon.geminitraveler.data.mappers.mapToMessageEntity
import com.ippon.geminitraveler.data.mappers.mapToPlanTravel
import com.ippon.geminitraveler.domain.model.ModelRequest
import com.ippon.geminitraveler.domain.model.ModelResponse
import com.ippon.geminitraveler.domain.model.Role
import com.ippon.geminitraveler.ui.mapper.mapToPlanTravelUi
import com.ippon.geminitraveler.ui.models.ModelEvent
import com.ippon.geminitraveler.ui.models.ModelResponseUiState

object ConstantsTestHelper {
    const val MODEL_RESPONSE = "response"
    const val REQUEST_PLAN_DATA = "request"

    val planTravelModel = ModelResponse(
        data = MODEL_RESPONSE,
        role = Role.MODEL
    )

    val requestPlan = ModelRequest(REQUEST_PLAN_DATA)

    val userResource: Resource<ModelResponse> = Resource.Success(
        requestPlan.mapToPlanTravel()
    )

    val modelResource: Resource<ModelResponse> = Resource.Success(
        planTravelModel
    )

    private const val ERROR_MESSAGE = "error"
    private val throwable = IllegalStateException(ERROR_MESSAGE)

    val errorResource: Resource<ModelResponse> = Resource.Error(
        errorMessage = ERROR_MESSAGE,
        throwable = throwable
    )

    val planTravels = listOf(planTravelModel)

    val messagesEntities = planTravels.map { it.mapToMessageEntity() }

    private val uiPlanTravels = planTravels.map { it.mapToPlanTravelUi() }

    val resourcePlanTravels = planTravels.map {
        Resource.Success(it)
    }

    val initialPlanTravelUiState = ModelResponseUiState()
    val successPlanTravelUiState = ModelResponseUiState(
        dataState = DataState.SUCCESS,
        planTravels = uiPlanTravels
    )
    val errorPlanTravelUiState = ModelResponseUiState(
        dataState = DataState.ERROR,
        errorMessage = ERROR_MESSAGE
    )

    val requestModelEvent = ModelEvent.ModelRequestEvent(
        prompt = REQUEST_PLAN_DATA
    )

    val messageEntity = planTravelModel.mapToMessageEntity()
}