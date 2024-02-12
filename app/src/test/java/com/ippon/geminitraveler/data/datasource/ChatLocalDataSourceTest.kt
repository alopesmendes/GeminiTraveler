package com.ippon.geminitraveler.data.datasource

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.ippon.geminitraveler.data.datasource.database.dao.ChatDao
import com.ippon.geminitraveler.data.datasource.database.entities.ChatEntityId
import com.ippon.geminitraveler.data.mappers.mapToChatEntity
import com.ippon.geminitraveler.domain.datasources.ChatDatasource
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
import org.mockito.kotlin.argThat
import org.mockito.kotlin.refEq
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class ChatLocalDataSourceTest {
    private val dispatcher = UnconfinedTestDispatcher()

    @Mock
    private lateinit var dao: ChatDao
    private lateinit var chatDatasource: ChatDatasource

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        chatDatasource = ChatLocalDataSource(dao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should add chat to database when using chat local datasource`() = runTest {
        // Given
        val chat = ConstantsTestHelper.chatRequest
        val chatEntity = ConstantsTestHelper.chatEntity
        val expectResult = 1L

        // When
        whenever(
            dao.insert(any())
        ).thenReturn(expectResult)
        val result = chatDatasource.insert(chat)

        // Then
        verify(dao, times(1)).insert(refEq(chatEntity))
        Truth.assertThat(result).isEqualTo(expectResult)
    }

    @Test
    fun `should return chats successfully when getting chats from local datasource`() = runTest {
        // Given
        val chats = ConstantsTestHelper.chats

        // When
        whenever(dao.findAllChats())
            .thenReturn(flowOf(ConstantsTestHelper.chatsEntities))
        val result = chatDatasource.getChats()

        // Then
        result.test {
            Truth.assertThat(awaitItem()).isEqualTo(chats)

            verify(dao, times(1)).findAllChats()
            awaitComplete()
        }
    }

    @Test
    fun `should delete chat from database when using chat local datasource`() = runTest {
        // Given
        val chatId = ConstantsTestHelper.CHAT_ID

        // When
        whenever(dao.deleteById(any())).thenReturn(Unit)
        chatDatasource.delete(chatId)

        // Then
        verify(dao, times(1))
            .deleteById(ChatEntityId(chatId))
    }

    @Test
    fun `should update chat from database when using chat local datasource`() = runTest {
        // Given
        val chat = ConstantsTestHelper.chat
        val expectResult = chat.mapToChatEntity()

        // When
        whenever(dao.update(any())).thenReturn(Unit)
        chatDatasource.update(chat)

        // Then
        verify(dao, times(1))
            .update(argThat {
                title == expectResult.title &&
                        id == expectResult.id
            })
    }
}