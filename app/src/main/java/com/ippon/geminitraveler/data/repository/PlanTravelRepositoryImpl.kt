package com.ippon.geminitraveler.data.repository

import com.ippon.geminitraveler.core.utils.Resource
import com.ippon.geminitraveler.data.mappers.mapToPlanTravel
import com.ippon.geminitraveler.domain.datasources.GenerativeDataSource
import com.ippon.geminitraveler.domain.model.PlanTravel
import com.ippon.geminitraveler.domain.model.RequestPlan
import com.ippon.geminitraveler.domain.model.Role
import com.ippon.geminitraveler.domain.repository.PlanTravelRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single

@Single
class PlanTravelRepositoryImpl(
    private val generativeDataSource: GenerativeDataSource,
): PlanTravelRepository {

    override fun getPlanTravel(requestPlan: RequestPlan): Flow<Resource<PlanTravel>> = flow {
        try {
            // User Input
            val userResource = Resource.Success(requestPlan.mapToPlanTravel())
            emit(userResource)

            // Waiting for AI model response
            emit(Resource.Loading)

            // AI model response
            val modelResponse = generativeDataSource.generateContent(requestPlan.data)
            emit(
                Resource.Success(
                    PlanTravel(
                        data = modelResponse ?: "",
                        role = Role.MODEL
                    )
                )
            )
        } catch (e: Exception) {
            emit(
                Resource.Error(
                    errorMessage = e.message,
                    throwable = e
                )
            )
        }
    }
}