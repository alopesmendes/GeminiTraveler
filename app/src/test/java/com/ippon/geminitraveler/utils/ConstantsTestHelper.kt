package com.ippon.geminitraveler.utils

import com.ippon.geminitraveler.core.utils.DataState
import com.ippon.geminitraveler.core.utils.Resource
import com.ippon.geminitraveler.data.mappers.mapToMessageEntity
import com.ippon.geminitraveler.data.mappers.mapToModelResponse
import com.ippon.geminitraveler.domain.model.ModelRequest
import com.ippon.geminitraveler.domain.model.ModelResponse
import com.ippon.geminitraveler.domain.model.Role
import com.ippon.geminitraveler.ui.mapper.mapToPlanTravelUi
import com.ippon.geminitraveler.ui.models.ModelEvent
import com.ippon.geminitraveler.ui.models.ModelResponseUiState

object ConstantsTestHelper {
    const val MODEL_RESPONSE = "response"
    const val MODEL_REQUEST_DATA = "request"

    val modelResponse = ModelResponse(
        data = MODEL_RESPONSE,
        role = Role.MODEL
    )

    val modelRequest = ModelRequest(MODEL_REQUEST_DATA)

    val userResource: Resource<ModelResponse> = Resource.Success(
        modelRequest.mapToModelResponse()
    )

    val modelResource: Resource<ModelResponse> = Resource.Success(
        modelResponse
    )

    private const val ERROR_MESSAGE = "error"
    val throwable = IllegalStateException(ERROR_MESSAGE)

    val modelResponseErrorResource: Resource<ModelResponse> = Resource.Error(
        errorMessage = ERROR_MESSAGE,
        throwable = throwable
    )

    val responses = listOf(modelResponse)

    val messagesEntities = responses.map { it.mapToMessageEntity() }

    private val uiPlanTravels = responses.map { it.mapToPlanTravelUi() }

    val resourcePlanTravels = responses.map {
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
        prompt = MODEL_REQUEST_DATA
    )

    val messageEntity = modelResponse.mapToMessageEntity()

    val resourceSuccess: Resource<Unit> = Resource.Success(Unit)

    val resourceError: Resource<Unit> = Resource.Error(
        throwable = throwable,
        errorMessage = ERROR_MESSAGE
    )
}