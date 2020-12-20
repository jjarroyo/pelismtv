package com.jj.pelismtv.model

import androidx.room.*
import java.util.*

@Entity(tableName = "episodes", primaryKeys = ["id"],
    foreignKeys = [(ForeignKey(entity = Season::class,
        parentColumns = ["id"],
        childColumns = ["season_id"],
        onDelete = ForeignKey.CASCADE))])
data class Episode(
    val id: Int,
    val name: String,
    val overview: String? = null,
    val episode_number:Int = 0,
    @TypeConverters(DateConverter::class)
    var air_date: Date? = null,
    val still_path: String? = null,
    val season_id:Int = 0,
    val updated_at: String
)

data class EpisodeSeason(
    @Embedded val episode: Episode,
    @Relation(
        parentColumn = "season_id",
        entity = Season::class,
        entityColumn = "id"
    )
    val  season: Season

)