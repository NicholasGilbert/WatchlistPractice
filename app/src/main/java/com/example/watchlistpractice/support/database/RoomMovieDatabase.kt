package com.example.watchlistpractice.support.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.watchlistpractice.data.RoomMovie

@Database(entities = arrayOf(RoomMovie::class), exportSchema = false, version = 1)
abstract class RoomMovieDatabase : RoomDatabase() {
    abstract fun DataDAO(): DataDAO

    companion object{
        lateinit var INSTANCE: RoomMovieDatabase

        operator fun invoke() =
            INSTANCE
    }
}