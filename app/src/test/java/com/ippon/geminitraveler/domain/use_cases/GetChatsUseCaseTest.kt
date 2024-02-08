package com.ippon.geminitraveler.domain.use_cases

import com.google.common.truth.Truth
import com.ippon.geminitraveler.domain.repository.ChatRepository
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
class GetChatsUseCaseTest {

    private val dispatcher = UnconfinedTestDispatcher()

    @Mock
    private lateinit var chatRepository: ChatRepository
    private lateinit var getChatsUseCase: GetChatsUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        getChatsUseCase = GetChatsUseCase(chatRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should update state chats when getting chats resource is successful`() = runTest {
        // Given
        val initialState = ConstantsTestHelper.initialChatsUiState
        val expectResult = ConstantsTestHelper.successChatsUiState
        var result = initialState

        // When
        whenever(chatRepository.getChats()).thenReturn(
            flowOf(ConstantsTestHelper.resourceSuccessChats)
        )
        getChatsUseCase.invoke(
            updateState = { state ->
                result = state.invoke(initialState)
            }
        )

        // Then
        verify(chatRepository, times(1)).getChats()
        Truth.assertThat(result.chats).isEqualTo(expectResult.chats)
    }

    @Test
    fun `should update to error state when getting chats resource fails`() = runTest {
        // Given
        val initialState = ConstantsTestHelper.initialChatsUiState
        val expectResult = ConstantsTestHelper.errorChatsUiState
        var result = initialState

        // When
        whenever(chatRepository.getChats()).thenReturn(
            flowOf(ConstantsTestHelper.resourceErrorChats)
        )
        getChatsUseCase.invoke(
            updateState = { state ->
                result = state.invoke(initialState)
            }
        )

        // Then
        verify(chatRepository, times(1)).getChats()
        Truth.assertThat(result).isEqualTo(expectResult)
    }
}