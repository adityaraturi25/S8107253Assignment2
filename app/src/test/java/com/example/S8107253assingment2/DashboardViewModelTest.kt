package com.example.S8107253assingment2

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

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
}
