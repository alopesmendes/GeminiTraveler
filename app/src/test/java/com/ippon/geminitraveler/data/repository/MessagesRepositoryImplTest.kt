package com.ippon.geminitraveler.data.repository

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.ippon.geminitraveler.data.mappers.mapToModelResponse
import com.ippon.geminitraveler.domain.datasources.GenerativeDataSource
import com.ippon.geminitraveler.domain.datasources.MessageDatasource
import com.ippon.geminitraveler.domain.model.Role
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
import org.mockito.kotlin.argThat
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.refEq
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class MessagesRepositoryImplTest {
    private val dispatcher = UnconfinedTestDispatcher()

    @Mock
    private lateinit var generativeDataSource: GenerativeDataSource
    @Mock
    private lateinit var messageDatasource: MessageDatasource
    private lateinit var messagesRepository: MessagesRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        messagesRepository = MessagesRepositoryImpl(
            generativeDataSource = generativeDataSource,
            messageDatasource = messageDatasource
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should return a flow of messages when asking to get messages`() = runTest {
        // Given
        val chatId = ConstantsTestHelper.CHAT_ID
        val messages = ConstantsTestHelper.responses
        val expectedResult = ConstantsTestHelper.resourceSuccessMessages

        // When
        whenever(messageDatasource.getMessagesFromChat(any()))
            .thenReturn(flowOf(messages))
        val result = messagesRepository.getMessagesFromChat(chatId)

        // Then
        result.test {
            Truth.assertThat(awaitItem()).isEqualTo(expectedResult)

            verify(messageDatasource, times(1))
                .getMessagesFromChat(refEq(chatId))
            awaitComplete()
        }
    }

    @Test
    fun `should return a flow Error when asking to get messages fails`() = runTest {
        // Given
        val chatId = ConstantsTestHelper.CHAT_ID
        val expectedResult = ConstantsTestHelper.resourceErrorMessages

        // When
        whenever(messageDatasource.getMessagesFromChat(any()))
            .thenThrow(ConstantsTestHelper.throwable)
        val result = messagesRepository.getMessagesFromChat(chatId)

        // Then
        result.test {
            Truth.assertThat(awaitItem()).isEqualTo(expectedResult)

            verify(messageDatasource, times(1))
                .getMessagesFromChat(refEq(chatId))
            awaitComplete()
        }
    }

    @Test
    fun `should return success resource when add messages user messages`() = runTest {
        // Given
        val modelRequest = ConstantsTestHelper.modelRequest
        val expectedResult = ConstantsTestHelper.resourceSuccessChatId

        // When
        whenever(messageDatasource.insertMessage(any())).thenReturn(ConstantsTestHelper.MESSAGE_USER_ID)
        val result = messagesRepository.addUserMessage(modelRequest)

        // Then
        Truth.assertThat(result).isEqualTo(expectedResult)

        verify(messageDatasource, atLeastOnce())
            .insertMessage(refEq(modelRequest.mapToModelResponse()))
    }

    @Test
    fun `should return error resource when exception is throw when user input fails`() = runTest {
        // Given
        val modelRequest = ConstantsTestHelper.modelRequest
        val expectedResult = ConstantsTestHelper.resourceErrorChatId

        // When
        whenever(messageDatasource.insertMessage(any()))
            .thenThrow(ConstantsTestHelper.throwable)
        val result = messagesRepository.addUserMessage(modelRequest)

        // Then
        Truth.assertThat(result).isEqualTo(expectedResult)

        verify(messageDatasource, atLeastOnce())
            .insertMessage(refEq(modelRequest.mapToModelResponse()))
    }

    @Test
    fun `should return success resource when add messages model generate message`() = runTest {
        // Given
        val modelRequest = ConstantsTestHelper.modelRequest
        val expectedResult = ConstantsTestHelper.resourceSuccessChatId

        // When
        whenever(messageDatasource.insertMessage(any()))
            .thenReturn(ConstantsTestHelper.MESSAGE_USER_ID)
        whenever(generativeDataSource.generateContent(any()))
            .thenReturn(ConstantsTestHelper.MODEL_RESPONSE)
        val result = messagesRepository.addModelMessage(
            modelRequest = modelRequest,
            messageParentId = ConstantsTestHelper.MESSAGE_USER_ID
        )

        // Then
        Truth.assertThat(result).isEqualTo(expectedResult)
        verify(generativeDataSource, times(1))
            .generateContent(refEq(modelRequest.data))
        verify(messageDatasource, times(1))
            .insertMessage(
                argThat {
                    data == ConstantsTestHelper.MODEL_RESPONSE && role == Role.MODEL
                }
            )
    }

    @Test
    fun `should return error resource when exception is throw model cannot generate response`() = runTest {
        // Given
        val modelRequest = ConstantsTestHelper.modelRequest
        val expectedResult = ConstantsTestHelper.resourceErrorChatId

        // When
        whenever(generativeDataSource.generateContent(any()))
            .thenThrow(ConstantsTestHelper.throwable)

        val result = messagesRepository.addModelMessage(
            modelRequest = modelRequest,
            messageParentId = ConstantsTestHelper.MESSAGE_USER_ID
        )

        // Then
        Truth.assertThat(result).isEqualTo(expectedResult)
        verify(generativeDataSource, times(1))
            .generateContent(modelRequest.data)
        verify(messageDatasource, times(0))
            .insertMessage(ConstantsTestHelper.modelResponse)
    }
}