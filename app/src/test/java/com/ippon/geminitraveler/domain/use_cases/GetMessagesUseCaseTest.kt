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
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class GetMessagesUseCaseTest {
    private val dispatcher = UnconfinedTestDispatcher()

    @Mock
    private lateinit var messagesRepository: MessagesRepository
    private lateinit var getMessagesUseCase: GetMessagesUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        getMessagesUseCase = GetMessagesUseCase(messagesRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should return success model response state when repository response is successful`() = runTest {
        // Given
        val result = ConstantsTestHelper.successModelResponseUiState

        // When
        whenever(
            messagesRepository.getMessages()
        ).thenReturn(
            flowOf(ConstantsTestHelper.resourceSuccessMessages)
        )

        val response = getMessagesUseCase(
            uiState = ConstantsTestHelper.initialModelResponseUiState
        )

        // Then
        response.test {
            Truth.assertThat(awaitItem()).isEqualTo(result)
            awaitComplete()

            verify(messagesRepository, times(1))
                .getMessages()
        }
    }

    @Test
    fun `should return error model response state when repository response is not successful`() = runTest {
        // Given
        val result = ConstantsTestHelper.errorModelResponseUiState

        // When
        whenever(
            messagesRepository.getMessages()
        ).thenReturn(flowOf(ConstantsTestHelper.resourceErrorMessages))

        val response = getMessagesUseCase(
            uiState = ConstantsTestHelper.initialModelResponseUiState
        )

        // Then
        response.test {
            Truth.assertThat(awaitItem()).isEqualTo(result)
            awaitComplete()

            verify(messagesRepository, times(1))
                .getMessages()
        }
    }
}