package com.ippon.geminitraveler.domain.repository

import com.ippon.geminitraveler.core.utils.Resource
import com.ippon.geminitraveler.domain.model.ModelResponse
import com.ippon.geminitraveler.domain.model.ModelRequest
import kotlinx.coroutines.flow.Flow

interface ModelRepository {
    fun getPlanTravel(requestPlan: ModelRequest): Flow<Resource<ModelResponse>>
}