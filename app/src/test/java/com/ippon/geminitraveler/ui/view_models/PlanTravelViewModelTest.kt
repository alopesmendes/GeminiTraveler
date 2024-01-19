package com.ippon.geminitraveler.ui.view_models

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.ippon.geminitraveler.core.utils.State
import com.ippon.geminitraveler.domain.model.PlanTravel
import com.ippon.geminitraveler.domain.model.RequestPlan
import com.ippon.geminitraveler.domain.use_cases.GetPlanTravelUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.refEq
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class PlanTravelViewModelTest {
    private val dispatcher = UnconfinedTestDispatcher()

    @Mock
    private lateinit var getPlanTravelUseCase: GetPlanTravelUseCase
    private lateinit var viewModel: PlanTravelViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        viewModel = PlanTravelViewModel(getPlanTravelUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should update successfully plan travel ui state when use case response is successful`() = runTest {
        // Given
        val planTravel = PlanTravel("")
        val prompt = ""
        val result: State<PlanTravel> = State.Success(data = planTravel)

        // When
        whenever(
            getPlanTravelUseCase.invoke(any())
        ).thenReturn(flowOf(result))

        viewModel.requestPlanTravel(prompt)

        // Then
        verify(getPlanTravelUseCase, atLeastOnce()).invoke(
            refEq(prompt)
        )

        val values = mutableListOf<State<PlanTravel>>()
        val job = launch(dispatcher) {
            viewModel.uiState.toList(values)
        }

        Truth.assertThat(values[0]).isEqualTo(result)
        job.cancel()
    }

    @Test
    fun `should update error plan travel ui state when use case response is not successful`() = runTest {
        // Given
        val prompt = ""
        val error = IllegalStateException("error")
        val result: State<PlanTravel> = State.Error(
            errorMessage = error.message,
            throwable = error
        )

        // When
        whenever(
            getPlanTravelUseCase.invoke(any())
        ).thenReturn(flowOf(result))

        viewModel.requestPlanTravel(prompt)

        // Then
        verify(getPlanTravelUseCase, atLeastOnce()).invoke(
            refEq(prompt)
        )

        val values = mutableListOf<State<PlanTravel>>()
        val job = launch(dispatcher) {
            viewModel.uiState.toList(values)
        }

        Truth.assertThat(values[0]).isEqualTo(result)
        job.cancel()
    }

}