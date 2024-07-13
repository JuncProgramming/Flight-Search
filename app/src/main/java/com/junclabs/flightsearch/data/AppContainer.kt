package com.junclabs.flightsearch.data

import android.content.Context

interface AppContainer {
    val repository: OfflineFlightsRepository
    val database: FlightsDatabase
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val database: FlightsDatabase by lazy { FlightsDatabase.getDatabase(context) }
    override val repository: OfflineFlightsRepository by lazy { OfflineFlightsRepository(database.flightsDao()) }
}