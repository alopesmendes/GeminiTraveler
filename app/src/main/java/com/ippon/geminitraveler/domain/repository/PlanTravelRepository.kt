package com.ippon.geminitraveler.domain.repository

import com.ippon.geminitraveler.domain.model.PlanTravel
import com.ippon.geminitraveler.domain.model.RequestPlan

interface PlanTravelRepository {
    suspend fun getPlanTravel(requestPlan: RequestPlan): PlanTravel
}