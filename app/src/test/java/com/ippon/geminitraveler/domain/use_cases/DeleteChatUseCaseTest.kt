package com.ippon.geminitraveler.domain.use_cases

import com.google.common.truth.Truth
import com.ippon.geminitraveler.core.utils.DataState
import com.ippon.geminitraveler.domain.repository.ChatRepository
import com.ippon.geminitraveler.ui.models.ChatUiState
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
class DeleteChatUseCaseTest {
    private val dispatcher = UnconfinedTestDispatcher()

    @Mock
    private lateinit var chatRepository: ChatRepository
    private lateinit var deleteChatUseCase: DeleteChatUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        deleteChatUseCase = DeleteChatUseCase(chatRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should update state with delete chat when using chat repository delete succeed`() = runTest {
        // Given
        val chatId = ConstantsTestHelper.CHAT_ID
        val initialState = ChatUiState()
        val expectedResult = ChatUiState(
            lastDeleteChatId = chatId,
            dataState = DataState.SUCCESS
        )
        var result = ChatUiState()

        // When
        whenever(chatRepository.deleteChat(any()))
            .thenReturn(ConstantsTestHelper.resourceSuccessChatId)
        deleteChatUseCase.invoke(
            chatId = chatId,
            updateState = { state ->
                result = state.invoke(initialState)
            }
        )

        // Then
        verify(chatRepository, times(1))
            .deleteChat(refEq(chatId))
        Truth.assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `should update state with error when using chat repository delete fails`() = runTest {
        // Given
        val chatId = ConstantsTestHelper.CHAT_ID
        val initialState = ChatUiState()
        val expectedResult = ConstantsTestHelper.errorChatsUiState
        var result = ChatUiState()

        // When
        whenever(chatRepository.deleteChat(any()))
            .thenReturn(ConstantsTestHelper.resourceErrorChatId)
        deleteChatUseCase.invoke(
            chatId = chatId,
            updateState = { state ->
                result = state.invoke(initialState)
            }
        )

        // Then
        verify(chatRepository, times(1))
            .deleteChat(refEq(chatId))
        Truth.assertThat(result).isEqualTo(expectedResult)
    }
}