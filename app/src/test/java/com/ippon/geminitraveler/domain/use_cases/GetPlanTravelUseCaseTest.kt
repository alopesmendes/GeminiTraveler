package com.ippon.geminitraveler.domain.use_cases

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.ippon.geminitraveler.core.utils.Resource
import com.ippon.geminitraveler.domain.model.PlanTravel
import com.ippon.geminitraveler.domain.model.RequestPlan
import com.ippon.geminitraveler.domain.repository.PlanTravelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.refEq
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class GetPlanTravelUseCaseTest {
    private val dispatcher = UnconfinedTestDispatcher()

    @Mock
    private lateinit var planTravelRepository: PlanTravelRepository
    private lateinit var getPlanTravelUseCase: GetPlanTravelUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        getPlanTravelUseCase = GetPlanTravelUseCase(planTravelRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should return success plan travel state when repository response is successful`() = runTest {
        // Given
        val planTravel = PlanTravel("")
        val prompt = ""
        val requestPlan = RequestPlan(prompt)
        val result: Resource<PlanTravel> = Resource.Success(data = planTravel)

        // When
        whenever(
            planTravelRepository.getPlanTravel(any())
        ).thenReturn(planTravel)

        val response = getPlanTravelUseCase(prompt)

        // Then
        response.test {
            Truth.assertThat(awaitItem()).isEqualTo(Resource.Loading)
            Truth.assertThat(awaitItem()).isEqualTo(result)
            awaitComplete()

            verify(planTravelRepository, times(1))
                .getPlanTravel(refEq(requestPlan))
        }

    }

    @Test
    fun `should return error plan travel state when repository response is not successful`() = runTest {
        // Given
        val prompt = ""
        val requestPlan = RequestPlan(prompt)
        val error = IllegalArgumentException("error")
        val result: Resource<PlanTravel> = Resource.Error(
            errorMessage = error.message,
            throwable = error,
        )

        // When
        whenever(
            planTravelRepository.getPlanTravel(any())
        ).thenThrow(error)

        val response = getPlanTravelUseCase(prompt)

        // Then
        response.test {
            Truth.assertThat(awaitItem()).isEqualTo(Resource.Loading)
            Truth.assertThat(awaitItem()).isEqualTo(result)
            awaitComplete()

            verify(planTravelRepository, times(1))
                .getPlanTravel(refEq(requestPlan))
        }

    }
}