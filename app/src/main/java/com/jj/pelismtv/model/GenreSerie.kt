package com.jj.pelismtv.model

import androidx.room.*
import com.google.gson.annotations.SerializedName


@Entity(tableName = "genres_serie", primaryKeys = ["id"])
data class GenreSerie(
    val id:Int,
    val label:String,
    val updated_at:String
)


@Entity(tableName = "serie_genre_tv",
    primaryKeys = ["serie_id", "genre_serie_id"],
    foreignKeys = [
        ForeignKey(
            entity = Serie::class,
            parentColumns = ["id"],
            childColumns = ["serie_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = GenreSerie::class,
            parentColumns = ["id"],
            childColumns = ["genre_serie_id"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class SerieGenre(
    val serie_id: Int,
    @SerializedName("label_tv_id")
    val genre_serie_id: Int,
    val updated_at: String
)

data class SerieWithGenres(
    @Embedded val serie: Serie,
    @Relation(
        parentColumn = "id",
        entity = GenreSerie::class,
        entityColumn = "id",
        associateBy = Junction(
            value = SerieGenre::class,parentColumn = "serie_id",entityColumn = "genre_serie_id"
        )
    )
    val genres: List<GenreSerie>
)

data class GenreWithSeries(
    @Embedded val genre: GenreSerie,
    @Relation(
        parentColumn = "id",
        entity = Serie::class,
        entityColumn = "id",
        associateBy = Junction(
            value = SerieGenre::class,parentColumn = "genre_serie_id",entityColumn = "serie_id"
        )
    )
    val series: List<Serie>

)