package com.ippon.geminitraveler.data.repository

import com.google.ai.client.generativeai.GenerativeModel
import com.ippon.geminitraveler.domain.datasources.GenerativeDataSource
import com.ippon.geminitraveler.domain.model.PlanTravel
import com.ippon.geminitraveler.domain.model.RequestPlan
import com.ippon.geminitraveler.domain.repository.PlanTravelRepository

class PlanTravelRepositoryImpl(
    private val generativeDataSource: GenerativeDataSource,
): PlanTravelRepository {

    override suspend fun getPlanTravel(requestPlan: RequestPlan): PlanTravel {
        val response = generativeDataSource.generateContent(requestPlan.data)
        return  PlanTravel(response ?: "")
    }
}