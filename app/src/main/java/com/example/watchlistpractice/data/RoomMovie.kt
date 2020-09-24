package com.example.watchlistpractice.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "app_table")
class RoomMovie (@PrimaryKey    @ColumnInfo(name = "id")            var roomMovieId : Int,
                                @ColumnInfo(name = "title")         var title       : String,
                                @ColumnInfo(name = "rating")        var rating      : Double,
                                @ColumnInfo(name = "release")       var release     : String,
                                @ColumnInfo(name = "language")      var language    : String,
                                 @ColumnInfo(name = "description")  var description : String)