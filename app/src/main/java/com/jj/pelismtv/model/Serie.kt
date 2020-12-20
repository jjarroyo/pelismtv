package com.jj.pelismtv.model

import androidx.room.Entity
import androidx.room.TypeConverters
import java.util.*

@Entity(tableName = "series", primaryKeys = ["id"])
data class Serie(
    val id: Int,
    val title: String,
    val overview: String? = null,
    @TypeConverters(DateConverter::class)
    var first_air_date: Date,
    val poster_path: String,
    val trailer: String? = null,
    val number_of_seasons:Int = 0,
    val updated_at: String
)