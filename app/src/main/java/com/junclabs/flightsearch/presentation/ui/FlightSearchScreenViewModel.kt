package com.junclabs.flightsearch.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.junclabs.flightsearch.FlightsApplication
import com.junclabs.flightsearch.data.OfflineFlightsRepository
import com.junclabs.flightsearch.data.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FlightSearchScreenViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val flightsRepository: OfflineFlightsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = userPreferencesRepository.isFavorite.map { isFavorite ->
        UiState(isFavorite)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UiState()
    )

    private val _uiEvent = Channel<FlightsEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun toggleFavorite(isFavorite: Boolean) {
        _uiState.value = UiState(isFavorite)
        viewModelScope.launch {
            userPreferencesRepository.saveFavorite(isFavorite)
        }
    }

    fun onEvent(event: FlightsEvent) {
        when (event) {
            is FlightsEvent.OnFlightSearch -> {
                flightsRepository.getFlightByIataOrName(event.query)
            }

            is FlightsEvent.OnFavoriteClick -> {
                _uiState.value = UiState(event.isFavorite)
                viewModelScope.launch {
                    userPreferencesRepository.saveFavorite(isFavorite = event.isFavorite)
                }
            }
        }
    }

    private fun sendUiEvent(event: FlightsEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiEvent.send(event)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as FlightsApplication)
                FlightSearchScreenViewModel(
                    application.userPreferencesRepository, application.container.repository
                )
            }
        }
    }
}