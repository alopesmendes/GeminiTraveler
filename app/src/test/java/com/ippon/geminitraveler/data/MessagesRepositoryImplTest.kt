package com.ippon.geminitraveler.data

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.ippon.geminitraveler.core.utils.Resource
import com.ippon.geminitraveler.data.mappers.mapToModelResponse
import com.ippon.geminitraveler.data.repository.MessagesRepositoryImpl
import com.ippon.geminitraveler.domain.datasources.GenerativeDataSource
import com.ippon.geminitraveler.domain.datasources.MessageDatasource
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
    fun `should return successful plan travel when request plan is good`() = runTest {
        // Given
        val modelResponse = ConstantsTestHelper.modelResponse
        val modelRequest = ConstantsTestHelper.modelRequest

        // When
        whenever(
            generativeDataSource.generateContent(any())
        ).thenReturn(ConstantsTestHelper.MODEL_RESPONSE)

        val resourceFlow = messagesRepository.getMessages(modelRequest)

        // Then
        resourceFlow.test {
            val userResource = awaitItem()
            val loadingResource = awaitItem()
            val modelResource = awaitItem()

            // Starts with User Model Resource
            Truth.assertThat(userResource).isInstanceOf(Resource.Success::class.java)
            Truth.assertThat(userResource).isEqualTo(ConstantsTestHelper.userResource)

            // Loading Resource afterwards
            Truth.assertThat(loadingResource).isInstanceOf(Resource.Loading::class.java)

            // Finally the response from the model
            Truth.assertThat(modelResource).isInstanceOf(Resource.Success::class.java)
            Truth.assertThat(modelResource).isEqualTo(ConstantsTestHelper.modelResource)

            verify(generativeDataSource, times(1))
                .generateContent(refEq(ConstantsTestHelper.MODEL_REQUEST_DATA))
            verify(messageDatasource, atLeastOnce())
                .addMessage(refEq(modelRequest.mapToModelResponse()))
            verify(messageDatasource, atLeastOnce())
                .addMessage(refEq(modelResponse))

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