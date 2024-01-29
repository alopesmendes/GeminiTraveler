package com.ippon.geminitraveler.domain.repository

import com.ippon.geminitraveler.core.utils.Resource
import com.ippon.geminitraveler.domain.model.PlanTravel
import com.ippon.geminitraveler.domain.model.RequestPlan
import kotlinx.coroutines.flow.Flow

interface PlanTravelRepository {
    fun getPlanTravel(requestPlan: RequestPlan): Flow<Resource<PlanTravel>>
}