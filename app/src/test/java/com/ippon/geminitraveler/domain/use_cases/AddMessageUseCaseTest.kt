package com.ippon.geminitraveler.domain.use_cases

import com.google.common.truth.Truth
import com.ippon.geminitraveler.domain.repository.MessagesRepository
import com.ippon.geminitraveler.utils.ConstantsTestHelper
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
class AddMessageUseCaseTest {
    private val dispatcher = UnconfinedTestDispatcher()

    @Mock
    private lateinit var messagesRepository: MessagesRepository
    private lateinit var addMessageUseCase: AddMessageUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        addMessageUseCase = AddMessageUseCase(messagesRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should return success model response state when repository response is successful`() = runTest {
        // Given
        val prompt = ConstantsTestHelper.MODEL_REQUEST_DATA
        val userMessage = ConstantsTestHelper.modelRequest
        val initialUiState = ConstantsTestHelper.initialMessagesUiState
            .copy(messages = ConstantsTestHelper.uiMessages)
        val expectedResult = ConstantsTestHelper.successMessagesUiState

        // When
        whenever(
            messagesRepository.addUserAndModelMessages(any())
        ).thenReturn(
            ConstantsTestHelper.resourceSuccess
        )

        val result = addMessageUseCase(
            prompt = prompt,
            uiState = initialUiState
        )

        // Then
        verify(messagesRepository, times(1))
            .addUserAndModelMessages(refEq(userMessage))
        Truth.assertThat(result.dataState).isEqualTo(expectedResult.dataState)
    }

    @Test
    fun `should return error model response state when repository response fails`() = runTest {
        // Given
        val prompt = ConstantsTestHelper.MODEL_REQUEST_DATA
        val userMessage = ConstantsTestHelper.modelRequest
        val initialUiState = ConstantsTestHelper.initialMessagesUiState
        val expectedResult = ConstantsTestHelper.errorMessagesUiState

        // When
        whenever(
            messagesRepository.addUserAndModelMessages(any())
        ).thenReturn(
            ConstantsTestHelper.resourceError
        )

        val result = addMessageUseCase(
            prompt = prompt,
            uiState = initialUiState
        )

        // Then
        verify(messagesRepository, times(1))
            .addUserAndModelMessages(refEq(userMessage))
        Truth.assertThat(result.dataState).isEqualTo(expectedResult.dataState)
        Truth.assertThat(result.errorMessage).isEqualTo(expectedResult.errorMessage)
    }
}