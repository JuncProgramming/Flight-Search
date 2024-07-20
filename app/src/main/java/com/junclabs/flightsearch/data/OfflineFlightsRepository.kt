package com.junclabs.flightsearch.data

import com.junclabs.flightsearch.domain.AirportItem
import kotlinx.coroutines.flow.Flow

class OfflineFlightsRepository(private val flightsDao: FlightsDao) : FlightsDao {
    override fun getFlightByIataOrName(query: String): Flow<List<AirportItem>> = flightsDao.getFlightByIataOrName(query)
}