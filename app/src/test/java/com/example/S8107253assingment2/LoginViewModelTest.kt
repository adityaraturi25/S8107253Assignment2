package com.example.S8107253assingment2

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

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
}
