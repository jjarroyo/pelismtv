package com.jj.pelismtv.data

import androidx.room.*
import com.jj.pelismtv.model.MovieGenre

@Dao
interface MovieGenreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend  fun insertMovieGenre(moviegenre: MovieGenre)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend  fun insertAllMovieGenre(moviegenre: List<MovieGenre>)

    @Update
    suspend  fun updateMovieGenre(moviegenre: MovieGenre)

    @Delete
    suspend  fun deleteMovieGenre(moviegenre: MovieGenre)
}