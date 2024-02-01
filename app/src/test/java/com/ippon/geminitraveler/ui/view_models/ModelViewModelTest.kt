package com.ippon.geminitraveler.ui.view_models

import com.google.common.truth.Truth
import com.ippon.geminitraveler.domain.use_cases.AddMessageUseCase
import com.ippon.geminitraveler.domain.use_cases.GetMessagesUseCase
import com.ippon.geminitraveler.ui.models.MessagesUiState
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
class ModelViewModelTest {
    private val dispatcher = UnconfinedTestDispatcher()

    @Mock
    private lateinit var getMessagesUseCase: GetMessagesUseCase
    @Mock
    private lateinit var addMessageUseCase: AddMessageUseCase
    private lateinit var viewModel: ModelViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        viewModel = ModelViewModel(
            getMessagesUseCase = getMessagesUseCase,
            addMessageUseCase = addMessageUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should update ui state when user send message succeed`() = runTest {
        // Given
        val prompt = ConstantsTestHelper.MODEL_REQUEST_DATA
        val exceptedResult = ConstantsTestHelper.successMessagesUiState

        // When
        whenever(
            addMessageUseCase(
                prompt = any(),
                uiState = any()
            )
        ).thenReturn(exceptedResult)

        viewModel.onHandleEvent(ConstantsTestHelper.userSendMessage)

        // Then
        verify(addMessageUseCase, atLeastOnce()).invoke(
            prompt = refEq(prompt),
            uiState = refEq(ConstantsTestHelper.initialMessagesUiState),
        )

        val values = mutableListOf<MessagesUiState>()
        val job = launch(dispatcher) {
            viewModel.uiState.toList(values)
        }


        Truth.assertThat(values[0]).isEqualTo(exceptedResult)
        job.cancel()
    }

    @Test
    fun `should update ui state when user send message failed`() = runTest {
        // Given
        val prompt = ConstantsTestHelper.MODEL_REQUEST_DATA
        val expectedResult = ConstantsTestHelper.successMessagesUiState

        // When
        whenever(
            addMessageUseCase.invoke(
                prompt = any(),
                uiState = any()
            )
        ).thenReturn(expectedResult)

        viewModel.onHandleEvent(ConstantsTestHelper.userSendMessage)

        // Then
        verify(addMessageUseCase, atLeastOnce()).invoke(
            prompt = refEq(prompt),
            uiState = refEq(ConstantsTestHelper.initialMessagesUiState)
        )

        val values = mutableListOf<MessagesUiState>()
        val job = launch(dispatcher) {
            viewModel.uiState.toList(values)
        }

        Truth.assertThat(values[0]).isEqualTo(expectedResult)
        job.cancel()
    }

    @Test
    fun `should update ui state when get messages succeed`() = runTest {
        // Given
        val expectedResult = ConstantsTestHelper.successMessagesUiState

        // When
        whenever(
            getMessagesUseCase.invoke(
                uiState = any(),
            )
        ).thenReturn(flowOf(expectedResult))

        viewModel.onHandleEvent(ConstantsTestHelper.getMessages)

        // Then
        verify(getMessagesUseCase, atLeastOnce()).invoke(
            uiState = refEq(ConstantsTestHelper.initialMessagesUiState)
        )

        val values = mutableListOf<MessagesUiState>()
        val job = launch(dispatcher) {
            viewModel.uiState.toList(values)
        }

        Truth.assertThat(values[0]).isEqualTo(expectedResult)
        job.cancel()
    }

    @Test
    fun `should update ui state when get messages failed`() = runTest {
        // Given
        val expectedResult = ConstantsTestHelper.errorMessagesUiState

        // When
        whenever(
            getMessagesUseCase.invoke(
                uiState = any(),
            )
        ).thenReturn(flowOf(expectedResult))

        viewModel.onHandleEvent(ConstantsTestHelper.getMessages)

        // Then
        verify(getMessagesUseCase, atLeastOnce()).invoke(
            uiState = refEq(ConstantsTestHelper.initialMessagesUiState)
        )

        val values = mutableListOf<MessagesUiState>()
        val job = launch(dispatcher) {
            viewModel.uiState.toList(values)
        }

        Truth.assertThat(values[0]).isEqualTo(expectedResult)
        job.cancel()
    }

}