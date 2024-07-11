package com.junclabs.flightsearch

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.junclabs.flightsearch.data.AppContainer
import com.junclabs.flightsearch.data.AppDataContainer
import com.junclabs.flightsearch.data.UserPreferencesRepository

private const val IS_FAVORITE_NAME = "is_favorite"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = IS_FAVORITE_NAME
)

class FlightsApplication : Application() {
    lateinit var userPreferencesRepository: UserPreferencesRepository
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        userPreferencesRepository = UserPreferencesRepository(dataStore)
        container = AppDataContainer(this)
    }
}