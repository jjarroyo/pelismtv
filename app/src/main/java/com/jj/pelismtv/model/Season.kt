package com.jj.pelismtv.model

import androidx.room.*
import java.util.*


@Entity(tableName = "seasons", primaryKeys = ["id"],
    foreignKeys = [(ForeignKey(entity = Serie::class,
        parentColumns = ["id"],
        childColumns = ["serie_id"],
        onDelete = ForeignKey.CASCADE))])
data class Season(
    val id: Int,
    val name: String,
    val overview: String? = null,
    val season_number:Int = 0,
    val episode_count:Int = 0,
    @TypeConverters(DateConverter::class)
    var air_date: Date? = null,
    val poster_path: String? = null,
    val serie_id:Int = 0,
    val updated_at: String
)
