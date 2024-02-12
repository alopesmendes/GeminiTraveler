package com.ippon.geminitraveler.domain.use_cases

import com.google.common.truth.Truth
import com.ippon.geminitraveler.domain.repository.ChatRepository
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
class AddChatUseCaseTest {
    private val dispatcher = UnconfinedTestDispatcher()

    @Mock
    private lateinit var chatRepository: ChatRepository
    private lateinit var addChatUseCase: AddChatUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        addChatUseCase = AddChatUseCase(chatRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should update to success state when adding chat resource is successful`() = runTest {
        // Given
        val initialUiState = ConstantsTestHelper.initialChatsUiState
            .copy(chats = ConstantsTestHelper.uiChats)
        val expectResult = ConstantsTestHelper.successChatsUiState
        val chatRequest = ConstantsTestHelper.chatRequest
        var result = initialUiState

        // When
        whenever(chatRepository.addChat(any()))
            .thenReturn(ConstantsTestHelper.resourceSuccessChatId)
        addChatUseCase.invoke(
            title = ConstantsTestHelper.CHAT_TITLE,
            createAt = ConstantsTestHelper.createAt,
            updateState = { state ->
                result = state.invoke(initialUiState)
            }
        )

        // Then
        Truth.assertThat(result).isEqualTo(expectResult)
        verify(chatRepository, times(1))
            .addChat(refEq( chatRequest))
    }

    @Test
    fun `should update to error state when adding chat resource fails`() = runTest {
        // Given
        val initialUiState = ConstantsTestHelper.initialChatsUiState
        val expectResult = ConstantsTestHelper.errorChatsUiState
        val chatRequest = ConstantsTestHelper.chatRequest
        var result = initialUiState

        // When
        whenever(chatRepository.addChat(any()))
            .thenReturn(ConstantsTestHelper.resourceErrorChatId)
        addChatUseCase.invoke(
            title = ConstantsTestHelper.CHAT_TITLE,
            createAt = ConstantsTestHelper.createAt,
            updateState = { state ->
                result = state.invoke(initialUiState)
            }
        )

        // Then
        Truth.assertThat(result).isEqualTo(expectResult)
        verify(chatRepository, times(1))
            .addChat(refEq( chatRequest))
    }
}