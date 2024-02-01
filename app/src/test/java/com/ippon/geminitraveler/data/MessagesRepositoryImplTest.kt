package com.ippon.geminitraveler.data

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.ippon.geminitraveler.data.mappers.mapToModelResponse
import com.ippon.geminitraveler.data.repository.MessagesRepositoryImpl
import com.ippon.geminitraveler.domain.datasources.GenerativeDataSource
import com.ippon.geminitraveler.domain.datasources.MessageDatasource
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
        val messages = ConstantsTestHelper.responses
        val expectedFirstResult = ConstantsTestHelper.resourceLoadingMessages
        val expectedSecondResult = ConstantsTestHelper.resourceSuccessMessages

        // When
        whenever(messageDatasource.getMessages())
            .thenReturn(flowOf(messages))
        val result = messagesRepository.getMessages()

        // Then
        result.test {
            Truth.assertThat(awaitItem()).isEqualTo(expectedFirstResult)
            Truth.assertThat(awaitItem()).isEqualTo(expectedSecondResult)

            verify(messageDatasource, times(1)).getMessages()
            awaitComplete()
        }
    }

    @Test
    fun `should return a flow Error when asking to get messages fails`() = runTest {
        // Given
        val expectedFirstResult = ConstantsTestHelper.resourceLoadingMessages
        val expectedSecondResult = ConstantsTestHelper.resourceErrorMessages

        // When
        whenever(messageDatasource.getMessages())
            .thenThrow(ConstantsTestHelper.throwable)
        val result = messagesRepository.getMessages()

        // Then
        result.test {
            Truth.assertThat(awaitItem()).isEqualTo(expectedFirstResult)
            Truth.assertThat(awaitItem()).isEqualTo(expectedSecondResult)

            verify(messageDatasource, times(1)).getMessages()
            awaitComplete()
        }
    }

    @Test
    fun `should return success resource when add messages user and model messages`() = runTest {
        // Given
        val modelRequest = ConstantsTestHelper.modelRequest
        val modelResponse = ConstantsTestHelper.modelResponse
        val expectedResult = ConstantsTestHelper.resourceSuccess

        // When
        whenever(messageDatasource.addMessage(any())).thenReturn(Unit)
        whenever(generativeDataSource.generateContent(any())).thenReturn(modelResponse.data)

        val result = messagesRepository.addUserAndModelMessages(modelRequest)

        // Then
        verify(messageDatasource, atLeastOnce())
            .addMessage(refEq(modelRequest.mapToModelResponse()))

        verify(generativeDataSource, atLeastOnce())
            .generateContent(modelRequest.data)

        verify(messageDatasource, atLeastOnce())
            .addMessage(refEq(modelResponse))

        Truth.assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `should return error resource when exception is throw when user input fails`() = runTest {
        // Given
        val modelRequest = ConstantsTestHelper.modelRequest
        val expectedResult = ConstantsTestHelper.resourceError

        // When
        whenever(messageDatasource.addMessage(any()))
            .thenThrow(ConstantsTestHelper.throwable)
        whenever(generativeDataSource.generateContent(any()))
            .thenReturn(modelRequest.data)

        val result = messagesRepository.addUserAndModelMessages(modelRequest)

        // Then
        verify(messageDatasource, atLeastOnce())
            .addMessage(modelRequest.mapToModelResponse())

        verify(generativeDataSource, times(0))
            .generateContent(modelRequest.data)

        Truth.assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `should return error resource when exception is throw model cannot generate response`() = runTest {
        // Given
        val modelRequest = ConstantsTestHelper.modelRequest
        val expectedResult = ConstantsTestHelper.resourceError

        // When
        whenever(messageDatasource.addMessage(any()))
            .thenReturn(Unit)

        whenever(generativeDataSource.generateContent(any()))
            .thenThrow(ConstantsTestHelper.throwable)

        val result = messagesRepository.addUserAndModelMessages(modelRequest)

        // Then
        verify(messageDatasource, atLeastOnce())
            .addMessage(modelRequest.mapToModelResponse())

        verify(generativeDataSource, atLeastOnce())
            .generateContent(modelRequest.data)

        Truth.assertThat(result).isEqualTo(expectedResult)
    }
}