package com.ippon.geminitraveler.data

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.ippon.geminitraveler.core.utils.Resource
import com.ippon.geminitraveler.data.repository.PlanTravelRepositoryImpl
import com.ippon.geminitraveler.domain.datasources.GenerativeDataSource
import com.ippon.geminitraveler.domain.repository.PlanTravelRepository
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
class PlanTravelRepositoryImplTest {
    private val dispatcher = UnconfinedTestDispatcher()

    @Mock
    private lateinit var generativeDataSource: GenerativeDataSource
    private lateinit var planTravelRepository: PlanTravelRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        planTravelRepository = PlanTravelRepositoryImpl(generativeDataSource)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should return successful plan travel when request plan is good`() = runTest {
        // Given
        val planTravel = ConstantsTestHelper.planTravelModel
        val requestPlan = ConstantsTestHelper.requestPlan

        // When
        whenever(
            generativeDataSource.generateContent(any())
        ).thenReturn(ConstantsTestHelper.MODEL_RESPONSE)

        val resourceFlow = planTravelRepository.getPlanTravel(requestPlan)

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
                .generateContent(refEq(ConstantsTestHelper.REQUEST_PLAN_DATA))

            awaitComplete()
        }
    }
}