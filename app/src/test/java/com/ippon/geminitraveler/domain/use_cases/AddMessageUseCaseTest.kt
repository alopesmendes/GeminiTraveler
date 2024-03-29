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
        val modelRequest = ConstantsTestHelper.modelRequest
        val initialState = ConstantsTestHelper.initialMessagesUiState
            .copy(messages = ConstantsTestHelper.uiMessages)
        val resourceSuccess = ConstantsTestHelper.resourceSuccessChatId
        val expectResult = ConstantsTestHelper.successMessagesUiState
        var result = ConstantsTestHelper.initialMessagesUiState

        // When
        whenever(messagesRepository.addUserMessage(any()))
            .thenReturn(resourceSuccess)
        whenever(messagesRepository.addModelMessage(any(), any()))
            .thenReturn(resourceSuccess)
        addMessageUseCase.invoke(
            prompt = prompt,
            createAt = ConstantsTestHelper.createAt,
            updateState = { state ->
                result = state.invoke(initialState)
            },
            chatId = ConstantsTestHelper.CHAT_ID,
        )

        // Then
        Truth.assertThat(result).isEqualTo(expectResult)
        verify(messagesRepository, times(1))
            .addUserMessage(refEq( modelRequest))
        verify(messagesRepository, times(1))
            .addModelMessage(
                modelRequest = refEq(modelRequest),
                messageParentId = refEq(ConstantsTestHelper.MESSAGE_USER_ID)
            )
    }

    @Test
    fun `should return error model response state when user repository response fails`() = runTest {
        // Given
        val prompt = ConstantsTestHelper.MODEL_REQUEST_DATA
        val modelRequest = ConstantsTestHelper.modelRequest
        val initialState = ConstantsTestHelper.initialMessagesUiState
        val resourceSuccess = ConstantsTestHelper.resourceSuccessChatId
        val resourceError = ConstantsTestHelper.resourceErrorChatId
        val expectResult = ConstantsTestHelper.errorMessagesUiState
        var result = ConstantsTestHelper.initialMessagesUiState

        // When
        whenever(messagesRepository.addUserMessage(any()))
            .thenReturn(resourceError)

        addMessageUseCase.invoke(
            prompt = prompt,
            createAt = ConstantsTestHelper.createAt,
            updateState = { state ->
                result = state.invoke(initialState)
            },
            chatId = ConstantsTestHelper.CHAT_ID
        )

        // Then
        Truth.assertThat(result).isEqualTo(expectResult)
        verify(messagesRepository, times(1))
            .addUserMessage(refEq( modelRequest))
        // Invoke 0 times if user message fails
        verify(messagesRepository, times(0))
            .addModelMessage(
                modelRequest = refEq(modelRequest),
                messageParentId = refEq(ConstantsTestHelper.MESSAGE_USER_ID)
            )
    }

    @Test
    fun `should return error model response state when model repository response fails`() = runTest {
        // Given
        val prompt = ConstantsTestHelper.MODEL_REQUEST_DATA
        val modelRequest = ConstantsTestHelper.modelRequest
        val initialState = ConstantsTestHelper.initialMessagesUiState
        val resourceSuccess = ConstantsTestHelper.resourceSuccessChatId
        val resourceError = ConstantsTestHelper.resourceErrorChatId
        val expectResult = ConstantsTestHelper.errorMessagesUiState
        var result = ConstantsTestHelper.initialMessagesUiState

        // When
        whenever(messagesRepository.addUserMessage(any()))
            .thenReturn(resourceSuccess)
        whenever(messagesRepository.addModelMessage(any(), any()))
            .thenReturn(resourceError)
        addMessageUseCase.invoke(
            prompt = prompt,
            createAt = ConstantsTestHelper.createAt,
            updateState = { state ->
                result = state.invoke(initialState)
            },
            chatId = ConstantsTestHelper.CHAT_ID
        )

        // Then
        Truth.assertThat(result).isEqualTo(expectResult)
        verify(messagesRepository, times(1))
            .addUserMessage(refEq( modelRequest))
        verify(messagesRepository, times(1))
            .addModelMessage(
                modelRequest = refEq(modelRequest),
                messageParentId = refEq(ConstantsTestHelper.MESSAGE_USER_ID)
            )
    }
}