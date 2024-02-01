package com.ippon.geminitraveler.ui.view_models

import com.google.common.truth.Truth
import com.ippon.geminitraveler.domain.use_cases.GetModelResponseUseCase
import com.ippon.geminitraveler.ui.models.ModelResponseUiState
import com.ippon.geminitraveler.utils.ConstantsTestHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
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
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.refEq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class ModelResponseViewModelTest {
    private val dispatcher = UnconfinedTestDispatcher()

    @Mock
    private lateinit var getPlanTravelUseCase: GetModelResponseUseCase
    private lateinit var viewModel: ModelViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        viewModel = ModelViewModel(getPlanTravelUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should update successfully plan travel ui state when use case response is successful`() = runTest {
        // Given
        val prompt = ConstantsTestHelper.MODEL_REQUEST_DATA
        val result = ConstantsTestHelper.successPlanTravelUiState

        // When
        whenever(
            getPlanTravelUseCase.invoke(
                prompt = any(),
                initialUiState = any()
            )
        ).thenReturn(flowOf(result))

        viewModel.onHandleEvent(ConstantsTestHelper.requestModelEvent)

        // Then
        verify(getPlanTravelUseCase, atLeastOnce()).invoke(
            prompt = refEq(prompt),
            initialUiState = refEq(ConstantsTestHelper.initialPlanTravelUiState),
        )

        val values = mutableListOf<ModelResponseUiState>()
        val job = launch(dispatcher) {
            viewModel.uiState.toList(values)
        }

        Truth.assertThat(values[0]).isEqualTo(result)
        job.cancel()
    }

    @Test
    fun `should update error plan travel ui state when use case response is not successful`() = runTest {
        // Given
        val prompt = ConstantsTestHelper.MODEL_REQUEST_DATA
        val result = ConstantsTestHelper.errorPlanTravelUiState

        // When
        whenever(
            getPlanTravelUseCase.invoke(
                prompt = any(),
                initialUiState = any(),
            )
        ).thenReturn(flowOf(result))

        viewModel.onHandleEvent(ConstantsTestHelper.requestModelEvent)

        // Then
        verify(getPlanTravelUseCase, atLeastOnce()).invoke(
            prompt = refEq(prompt),
            initialUiState = refEq(ConstantsTestHelper.initialPlanTravelUiState)
        )

        val values = mutableListOf<ModelResponseUiState>()
        val job = launch(dispatcher) {
            viewModel.uiState.toList(values)
        }

        Truth.assertThat(values[0]).isEqualTo(result)
        job.cancel()
    }

}