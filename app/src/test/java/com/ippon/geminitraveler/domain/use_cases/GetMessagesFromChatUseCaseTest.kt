package com.ippon.geminitraveler.domain.use_cases

import com.google.common.truth.Truth
import com.ippon.geminitraveler.core.utils.DataState
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
class GetMessagesFromChatUseCaseTest {
    private val dispatcher = UnconfinedTestDispatcher()

    @Mock
    private lateinit var messagesRepository: MessagesRepository
    private lateinit var getMessagesFromChatUseCase: GetMessagesFromChatUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        getMessagesFromChatUseCase = GetMessagesFromChatUseCase(messagesRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should return success model response state when repository response is successful`() = runTest {
        // Given
        val chatId = ConstantsTestHelper.CHAT_ID
        val initialState = ConstantsTestHelper.initialMessagesUiState
        val expectedResult = ConstantsTestHelper.successMessagesUiState
        var result = ConstantsTestHelper.initialMessagesUiState

        // When
        whenever(
            messagesRepository.getMessagesFromChat(any())
        ).thenReturn(
            flowOf(ConstantsTestHelper.resourceSuccessMessages)
        )
        getMessagesFromChatUseCase(
            chatId = chatId,
            updateState = { state ->
                result = state.invoke(
                    initialState.copy(
                        dataState = DataState.SUCCESS,
                        currentMessageId = ConstantsTestHelper.MESSAGE_USER_ID
                    )
                )
            }
        )

        // Then
        verify(messagesRepository, times(1))
            .getMessagesFromChat(refEq(chatId))
        Truth.assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `should return error model response state when repository response is not successful`() = runTest {
        // Given
        val chatId = ConstantsTestHelper.CHAT_ID
        val initialState = ConstantsTestHelper.initialMessagesUiState
        val expectedResult = ConstantsTestHelper.errorMessagesUiState
        var result = ConstantsTestHelper.initialMessagesUiState

        // When
        whenever(
            messagesRepository.getMessagesFromChat(any())
        ).thenReturn(flowOf(ConstantsTestHelper.resourceErrorMessages))
        getMessagesFromChatUseCase(
            chatId = chatId,
            updateState = { state ->
                result = state.invoke(initialState)
            }
        )

        // Then
        Truth.assertThat(result).isEqualTo(expectedResult)
    }
}