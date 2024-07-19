package com.junclabs.flightsearch.domain

import kotlinx.coroutines.flow.Flow

interface FlightsRepository {
    fun getFlightByIataOrName(query: String): Flow<List<AirportItem>>
}