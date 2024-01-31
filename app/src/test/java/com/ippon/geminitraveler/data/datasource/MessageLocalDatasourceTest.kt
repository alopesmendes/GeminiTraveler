package com.ippon.geminitraveler.data.datasource

import com.ippon.geminitraveler.data.datasource.database.dao.MessageDao
import com.ippon.geminitraveler.domain.datasources.MessageDatasource
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
class MessageLocalDatasourceTest {
    private val dispatcher = UnconfinedTestDispatcher()

    @Mock
    private lateinit var dao: MessageDao
    private lateinit var messageLocalDatasource: MessageDatasource

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        messageLocalDatasource = MessageLocalDatasource(dao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should add message to database when using message local datasource`() = runTest {
        // Given
        val message = ConstantsTestHelper.planTravelModel
        val messageEntity = ConstantsTestHelper.messageEntity

        // When
        whenever(
            dao.insert(any())
        ).thenReturn(Unit)
        messageLocalDatasource.addMessage(message)

        // Then
        verify(dao, times(1)).insert(refEq(messageEntity))
    }
}