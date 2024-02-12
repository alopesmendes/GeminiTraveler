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
class UpdateChatTitleUseCaseTest {
    private val dispatcher = UnconfinedTestDispatcher()

    @Mock
    private lateinit var chatRepository: ChatRepository
    private lateinit var updateChatTitleUseCase: UpdateChatTitleUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        updateChatTitleUseCase = UpdateChatTitleUseCase(chatRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should update state last update chat when using chat repository update title succeed`() = runTest {
        // Given
        val newTitle = "New title"
        val chatId = ConstantsTestHelper.CHAT_ID
        var result = ChatUiState()
        val expectedResult = ChatUiState(
            lastUpdateChatId = chatId,
            dataState = DataState.SUCCESS
        )

        // When
        whenever(chatRepository.updateChatTitle(any(), any()))
            .thenReturn(ConstantsTestHelper.resourceSuccessChatId)
        updateChatTitleUseCase.invoke(
            chatId = chatId,
            title = newTitle,
            updateState = { state ->
                result = state.invoke(result)
            }
        )

        // Then
        verify(chatRepository, times(1)).updateChatTitle(
            id = refEq(chatId),
            title = refEq(newTitle)
        )
        Truth.assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `should update state error state and message when using chat repository update title fails`() = runTest {
        // Given
        val chat = ConstantsTestHelper.chat
        var result = ChatUiState()
        val expectedResult = ConstantsTestHelper.errorChatsUiState

        // When
        whenever(chatRepository.updateChatTitle(any(), any()))
            .thenReturn(ConstantsTestHelper.resourceErrorChatId)
        updateChatTitleUseCase.invoke(
            chatId = chat.id,
            title = "New Title",
            updateState = { state ->
                result = state.invoke(result)
            }
        )

        // Then
        Truth.assertThat(result).isEqualTo(expectedResult)
    }
}