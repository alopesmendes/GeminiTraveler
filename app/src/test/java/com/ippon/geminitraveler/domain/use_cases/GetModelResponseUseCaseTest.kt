package com.ippon.geminitraveler.domain.use_cases

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.ippon.geminitraveler.domain.repository.MessagesRepository
import com.ippon.geminitraveler.utils.ConstantsTestHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
class GetModelResponseUseCaseTest {
    private val dispatcher = UnconfinedTestDispatcher()

    @Mock
    private lateinit var planTravelRepository: MessagesRepository
    private lateinit var getPlanTravelUseCase: GetModelResponseUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        getPlanTravelUseCase = GetModelResponseUseCase(planTravelRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should return success plan travel state when repository response is successful`() = runTest {
        // Given
        val prompt = ConstantsTestHelper.MODEL_REQUEST_DATA
        val requestPlan = ConstantsTestHelper.modelRequest
        val result = ConstantsTestHelper.successPlanTravelUiState

        // When
        whenever(
            planTravelRepository.getMessages(any())
        ).thenReturn(
            flowOf(*ConstantsTestHelper.resourcePlanTravels.toTypedArray())
        )

        val response = getPlanTravelUseCase(
            prompt = prompt,
            initialUiState = ConstantsTestHelper.initialPlanTravelUiState
        )

        // Then
        response.test {
            Truth.assertThat(awaitItem()).isEqualTo(result)
            awaitComplete()

            verify(planTravelRepository, times(1))
                .getMessages(refEq(requestPlan))
        }
    }

    @Test
    fun `should return error plan travel state when repository response is not successful`() = runTest {
        // Given
        val prompt = ConstantsTestHelper.MODEL_REQUEST_DATA
        val requestPlan = ConstantsTestHelper.modelRequest
        val result = ConstantsTestHelper.errorPlanTravelUiState

        // When
        whenever(
            planTravelRepository.getMessages(any())
        ).thenReturn(flowOf(ConstantsTestHelper.errorResource))

        val response = getPlanTravelUseCase(
            prompt = prompt,
            initialUiState = ConstantsTestHelper.initialPlanTravelUiState
        )

        // Then
        response.test {
            Truth.assertThat(awaitItem()).isEqualTo(result)
            awaitComplete()

            verify(planTravelRepository, times(1))
                .getMessages(refEq(requestPlan))
        }
    }
}