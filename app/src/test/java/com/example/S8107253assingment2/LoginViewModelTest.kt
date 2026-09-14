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

class LoginViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private class FakeAuthRepository : AuthRepository {
        var loginCalled = false

        override suspend fun login(username: String, password: String): String {
            loginCalled = true
            return "test-key"
        }
    }

    @Test
    fun invalidStudentIdShowsErrorWithoutCallingRepository() {
        val repository = FakeAuthRepository()
        val viewModel = LoginViewModel(repository)

        viewModel.login("abc", "Aditya")

        assertTrue(viewModel.state.value is LoginUiState.Error)
        assertFalse(repository.loginCalled)
    }

    @Test
    fun blankFirstNameShowsErrorWithoutCallingRepository() {
        val repository = FakeAuthRepository()
        val viewModel = LoginViewModel(repository)

        viewModel.login("1234567", "")

        assertTrue(viewModel.state.value is LoginUiState.Error)
        assertFalse(repository.loginCalled)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun validLoginShowsSuccessWithReturnedKey() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val repository = FakeAuthRepository()
            val viewModel = LoginViewModel(repository)

            viewModel.login("1234567", "Adi")
            advanceUntilIdle()

            assertTrue(repository.loginCalled)
            assertEquals(LoginUiState.Success("test-key"), viewModel.state.value)
        } finally {
            Dispatchers.resetMain()
        }
    }
}
