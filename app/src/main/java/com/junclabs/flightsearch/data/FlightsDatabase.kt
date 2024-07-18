package com.junclabs.flightsearch.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.junclabs.flightsearch.domain.AirportItem
import com.junclabs.flightsearch.domain.FavoriteItem

@Database(entities = [AirportItem::class, FavoriteItem::class], version = 1, exportSchema = false)
abstract class FlightsDatabase : RoomDatabase() {

    abstract fun flightsDao(): FlightsDao

    companion object {
        @Volatile
        private var Instance: FlightsDatabase? = null

        fun getDatabase(context: Context): FlightsDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, FlightsDatabase::class.java, "app_database")
                    .createFromAsset("database/flight_search.db").build().also { Instance = it }
            }
        }
    }
}