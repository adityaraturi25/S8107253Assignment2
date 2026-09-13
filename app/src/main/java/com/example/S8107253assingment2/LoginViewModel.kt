package com.example.S8107253assingment2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface LoginUiState {
    data object Idle : LoginUiState
    data object Loading : LoginUiState
    data class Success(val keypass: String) : LoginUiState
    data class Error(val message: String) : LoginUiState
}

class LoginViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _state = MutableLiveData<LoginUiState>(LoginUiState.Idle)
    val state: LiveData<LoginUiState> = _state

    fun login(username: String, password: String) {
        val studentId = username.trim()

        if (!studentId.matches(Regex("\\d{7,8}"))) {
            _state.value = LoginUiState.Error("Enter your student ID as digits only, without the s.")
            return
        }
        if (password.isBlank()) {
            _state.value = LoginUiState.Error("Enter your first name.")
            return
        }
        if (_state.value == LoginUiState.Loading) return

        _state.value = LoginUiState.Loading
        viewModelScope.launch {
            try {
                val keypass = repository.login(studentId, password)
                _state.value = if (keypass.isBlank()) {
                    LoginUiState.Error("The server did not return a dashboard key.")
                } else {
                    LoginUiState.Success(keypass)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: HttpException) {
                _state.value = LoginUiState.Error(
                    "Unable to sign in. Check your details or try again later."
                )
            } catch (e: IOException) {
                _state.value = LoginUiState.Error("Could not connect. Check your internet and try again.")
            } catch (e: Exception) {
                _state.value = LoginUiState.Error("Something went wrong. Please try again.")
            }
        }
    }
}