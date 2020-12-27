package com.jj.pelismtv.model

import androidx.room.*
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "genres", primaryKeys = ["id"])
data class Genre(
    val id:Int,
    val label:String,
    val updated_at:String
)


@Entity(tableName = "movie_genre",
    primaryKeys = ["movie_id", "genre_id"],
    foreignKeys = [
        ForeignKey(
            entity = Movie::class,
            parentColumns = ["id"],
            childColumns = ["movie_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Genre::class,
            parentColumns = ["id"],
            childColumns = ["genre_id"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class MovieGenre(
    val movie_id: Int,
    @SerializedName("label_id")
    val genre_id: Int,
    val updated_at: String
)

data class MovieWithGenres(
    @Embedded val movie: Movie,
    @Relation(
        parentColumn = "id",
        entity = Genre::class,
        entityColumn = "id",
        associateBy = Junction(
            value = MovieGenre::class,parentColumn = "movie_id",entityColumn = "genre_id"
        )
    )
    val genres: List<Genre>
)

data class GenreWithMovies(
        @Embedded val genre: Genre,
        @Relation(
                parentColumn = "id",
                entity = Movie::class,
                entityColumn = "id",
                associateBy = Junction(
                        value = MovieGenre::class,parentColumn = "genre_id",entityColumn = "movie_id"
                )
        )
        val movies: List<Movie>

)