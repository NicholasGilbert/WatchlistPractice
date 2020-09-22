package com.example.watchlistpractice.support

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.watchlistpractice.data.RoomMovie

@Database(entities = arrayOf(RoomMovie::class), exportSchema = false, version = 1)
abstract class RoomMovieDatabase : RoomDatabase() {
    abstract fun DataDAO(): DataDAO

    companion object{
        @Volatile
        lateinit var INSTANCE: RoomMovieDatabase

        val LOCK = Any()

        operator fun invoke(context: Context)= INSTANCE ?: synchronized(LOCK){
            INSTANCE ?: buildDatabase(context).also { INSTANCE = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            RoomMovieDatabase::class.java, "movie_list.db").build()
    }
}