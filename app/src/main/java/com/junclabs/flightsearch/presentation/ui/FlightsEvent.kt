package com.junclabs.flightsearch.presentation.ui

interface FlightsEvent {
    data class OnFlightSearch(val query: String) : FlightsEvent
    data class OnFavoriteClick(val isFavorite: Boolean) : FlightsEvent
}