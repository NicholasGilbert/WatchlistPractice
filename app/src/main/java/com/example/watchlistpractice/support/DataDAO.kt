package com.example.watchlistpractice.support

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.watchlistpractice.data.Movie
import com.example.watchlistpractice.data.RoomMovie

@Dao
interface DataDAO {
    @Query("SELECT * from app_table")
    fun getData(): List<RoomMovie>

    @Update
    fun update(data: List<RoomMovie>)

    @Insert
    fun insert(data: RoomMovie)

    @Query("DELETE FROM app_table WHERE id = :inId")
    fun delete(inId: Int)
}