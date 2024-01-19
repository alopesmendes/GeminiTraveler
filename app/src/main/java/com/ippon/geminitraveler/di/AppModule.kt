package com.ippon.geminitraveler.di

import com.google.ai.client.generativeai.GenerativeModel
import com.ippon.geminitraveler.BuildConfig
import com.ippon.geminitraveler.data.datasource.GenerativeDataSourceImpl
import com.ippon.geminitraveler.data.repository.PlanTravelRepositoryImpl
import com.ippon.geminitraveler.domain.datasources.GenerativeDataSource
import com.ippon.geminitraveler.domain.repository.PlanTravelRepository
import com.ippon.geminitraveler.domain.use_cases.GetPlanTravelUseCase
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val appModule = module {
    single {
        GenerativeModel(
            modelName = "gemini-pro",
            apiKey = BuildConfig.apiKey
        )
    }

    // Datasource's
    singleOf(::GenerativeDataSourceImpl) { bind<GenerativeDataSource>() }

    // Repository
    singleOf(::PlanTravelRepositoryImpl) { bind<PlanTravelRepository>() }

    // Use Case
    singleOf(::GetPlanTravelUseCase)
}