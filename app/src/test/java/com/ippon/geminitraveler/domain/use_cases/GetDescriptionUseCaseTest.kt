package com.ippon.geminitraveler.domain.use_cases

import com.google.common.truth.Truth
import com.ippon.geminitraveler.core.utils.DataState
import com.ippon.geminitraveler.domain.datasources.GenerativeDataSource
import com.ippon.geminitraveler.ui.models.HomeUiState
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
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class GetDescriptionUseCaseTest {
    private val dispatcher = UnconfinedTestDispatcher()

    @Mock
    private lateinit var generativeDataSource: GenerativeDataSource
    private lateinit var getDescriptionUseCase: GetDescriptionUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        getDescriptionUseCase = GetDescriptionUseCase(generativeDataSource)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `should update state description when getting is generate response is successful`() = runTest {
        // Given
        var result = HomeUiState()
        val expectedResult = HomeUiState(
            dataState = DataState.SUCCESS,
            description = ConstantsTestHelper.DESCRIPTION
        )

        // When
        whenever(generativeDataSource.generateContent(any()))
            .thenReturn(ConstantsTestHelper.DESCRIPTION)
        getDescriptionUseCase.invoke(
            prompt = ConstantsTestHelper.DESCRIPTION,
            updateState = { state ->
                result = state.invoke(result)
            }
        )

        // Then
        verify(generativeDataSource, times(1))
            .generateContent(ConstantsTestHelper.DESCRIPTION)
        Truth.assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `should update state error message when getting is generate response fails`() = runTest {
        // Given
        var result = HomeUiState()
        val expectedResult = HomeUiState(
            dataState = DataState.ERROR,
            errorMessage = ConstantsTestHelper.ERROR_MESSAGE
        )

        // When
        whenever(generativeDataSource.generateContent(any()))
            .thenThrow(ConstantsTestHelper.throwable)
        getDescriptionUseCase.invoke(
            prompt = ConstantsTestHelper.DESCRIPTION,
            updateState = { state ->
                result = state.invoke(result)
            }
        )

        // Then
        verify(generativeDataSource, times(1))
            .generateContent(ConstantsTestHelper.DESCRIPTION)
        Truth.assertThat(result).isEqualTo(expectedResult)
    }

}