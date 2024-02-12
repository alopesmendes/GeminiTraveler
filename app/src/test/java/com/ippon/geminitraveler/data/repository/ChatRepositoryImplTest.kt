package com.ippon.geminitraveler.data.repository

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.ippon.geminitraveler.domain.datasources.ChatDatasource
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
import org.mockito.kotlin.any
import org.mockito.kotlin.refEq
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class ChatRepositoryImplTest {
    private val dispatcher = UnconfinedTestDispatcher()

    @Mock
    private lateinit var chatDatasource: ChatDatasource
    private lateinit var chatRepository: ChatRepository
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        chatRepository = ChatRepositoryImpl(chatDatasource)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should return a flow of chats when asking to get chats`() = runTest {
        // Given
        val chats = ConstantsTestHelper.chats
        val expectedResult = ConstantsTestHelper.resourceSuccessChats

        // When
        whenever(chatDatasource.getChats()).thenReturn(flowOf(chats))
        val result = chatRepository.getChats()

        // Then
        result.test {
            Truth.assertThat(awaitItem()).isEqualTo(expectedResult)

            verify(chatDatasource, times(1)).getChats()
            awaitComplete()
        }
    }

    @Test
    fun `should return a error resource when asking to get chats fails`() = runTest {
        // Given
        val expectedResult = ConstantsTestHelper.resourceErrorChats

        // When
        whenever(chatDatasource.getChats()).thenThrow(ConstantsTestHelper.throwable)
        val result = chatRepository.getChats()

        // Then
        result.test {
            Truth.assertThat(awaitItem()).isEqualTo(expectedResult)

            verify(chatDatasource, times(1)).getChats()
            awaitComplete()
        }
    }

    @Test
    fun `should return success resource when adding chat succeed`() = runTest {
        // Given
        val chat = ConstantsTestHelper.chatRequest
        val expectResult = ConstantsTestHelper.resourceSuccessChatId

        // When
        whenever(chatDatasource.insert(any()))
            .thenReturn(ConstantsTestHelper.CHAT_ID)
        val result = chatRepository.addChat(chat)

        // Then
        verify(chatDatasource, times(1))
            .insert(refEq(chat))
        Truth.assertThat(result).isEqualTo(expectResult)

    }

    @Test
    fun `should return error resource when adding chat failed`() = runTest {
        // Given
        val chat = ConstantsTestHelper.chatRequest
        val expectResult = ConstantsTestHelper.resourceErrorChatId

        // When
        whenever(chatDatasource.insert(any()))
            .thenThrow(ConstantsTestHelper.throwable)
        val result = chatRepository.addChat(chat)

        // Then
        verify(chatDatasource, times(1))
            .insert(refEq(chat))
        Truth.assertThat(result).isEqualTo(expectResult)
    }

    @Test
    fun `should return success resource when deleting chat succeed`() = runTest {
        // Given
        val chatId = ConstantsTestHelper.CHAT_ID
        val expectedResult = ConstantsTestHelper.resourceSuccessChatId

        // When
        whenever(chatDatasource.delete(any())).thenReturn(Unit)
        val result = chatRepository.deleteChat(chatId)

        // Then
        verify(chatDatasource, times(1)).delete(refEq(chatId))
        Truth.assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `should return error resource when deleting chat fails`() = runTest {
        // Given
        val chat = ConstantsTestHelper.CHAT_ID
        val expectedResult = ConstantsTestHelper.resourceErrorChatId

        // When
        whenever(chatDatasource.delete(any())).thenThrow(ConstantsTestHelper.throwable)
        val result = chatRepository.deleteChat(chat)

        // Then
        verify(chatDatasource, times(1)).delete(refEq(chat))
        Truth.assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `should return success resource when updating chat succeed`() = runTest {
        // Given
        val chat = ConstantsTestHelper.chat
        val expectedResult = ConstantsTestHelper.resourceSuccessChat

        // When
        whenever(chatDatasource.updateTitle(any(), any())).thenReturn(Unit)
        val result = chatRepository.updateChatTitle(
            id = chat.id,
            title = chat.title
        )

        // Then
        verify(chatDatasource, times(1)).updateTitle(
            id = refEq(chat.id),
            title = refEq(chat.title)
        )
        Truth.assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `should return error resource when updating chat fails`() = runTest {
        // Given
        val chat = ConstantsTestHelper.chat
        val expectedResult = ConstantsTestHelper.resourceErrorChat

        // When
        whenever(chatDatasource.updateTitle(any(), any())).thenThrow(ConstantsTestHelper.throwable)
        val result = chatRepository.updateChatTitle(
            id = chat.id,
            title = chat.title
        )

        // Then
        verify(chatDatasource, times(1)).updateTitle(
            id = refEq(chat.id),
            title = refEq(chat.title)
        )
        Truth.assertThat(result).isEqualTo(expectedResult)
    }
}