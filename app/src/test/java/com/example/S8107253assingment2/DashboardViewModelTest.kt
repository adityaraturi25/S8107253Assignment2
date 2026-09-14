package com.example.S8107253assingment2

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals

class DashboardViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private class FakeDashboardRepository : DashboardRepository {
        var fetchCalled = false

        override suspend fun getDashboard(keypass: String): DashboardResponse {
            fetchCalled = true
            return DashboardResponse(emptyList(), 0)
        }
    }

    @Test
    fun missingKeyShowsErrorWithoutFetchingDashboard() {
        val repository = FakeDashboardRepository()
        val viewModel = DashboardViewModel(repository)

        viewModel.load("")

        assertTrue(viewModel.state.value is DashboardUiState.Error)
        assertFalse(repository.fetchCalled)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun validKeyLoadsDashboardFromRepository() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val repository = FakeDashboardRepository()
            val viewModel = DashboardViewModel(repository)

            viewModel.load("test-key")
            advanceUntilIdle()

            assertTrue(repository.fetchCalled)
            assertEquals(
                DashboardUiState.Success(DashboardResponse(emptyList(), 0)),
                viewModel.state.value
            )
        } finally {
            Dispatchers.resetMain()
        }
    }
}
