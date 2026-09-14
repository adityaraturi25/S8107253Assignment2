package com.example.S8107253assingment2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

sealed interface DashboardUiState {
    data object Idle : DashboardUiState
    data object Loading : DashboardUiState
    data class Success(val response: DashboardResponse) : DashboardUiState
    data class Error(val message: String) : DashboardUiState
}

class DashboardViewModel(
    private val repository: DashboardRepository
) : ViewModel() {

    private val _state = MutableLiveData<DashboardUiState>(DashboardUiState.Idle)
    val state: LiveData<DashboardUiState> = _state

    fun load(keypass: String) {
        if (keypass.isBlank()) {
            _state.value = DashboardUiState.Error("Dashboard key is missing. Please log in again.")
            return
        }
        if (_state.value == DashboardUiState.Loading) return

        _state.value = DashboardUiState.Loading
        viewModelScope.launch {
            try {
                _state.value = DashboardUiState.Success(repository.getDashboard(keypass))
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.value = DashboardUiState.Error("Could not load items. Please try again.")
            }
        }
    }
}