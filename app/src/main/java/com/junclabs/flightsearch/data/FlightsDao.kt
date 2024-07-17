package com.junclabs.flightsearch.data

import androidx.room.Dao
import androidx.room.Query
import com.junclabs.flightsearch.domain.AirportItem
import kotlinx.coroutines.flow.Flow

@Dao
interface FlightsDao {
    @Query("SELECT * from airport WHERE iata_code OR name LIKE '%query%' ORDER BY passengers DESC")
    fun getFlightByIataOrName(query: String): Flow<List<AirportItem>>
}