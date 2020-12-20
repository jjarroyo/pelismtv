package com.jj.pelismtv.data

import androidx.room.*
import com.jj.pelismtv.model.Genre
import com.jj.pelismtv.model.GenreSerie

@Dao
interface GenreTvDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend  fun insertGenre(genre: GenreSerie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend  fun insertAllGenre(genre: List<GenreSerie>)

    @Update
    suspend  fun updateGenre(genre: GenreSerie)

    @Delete
    suspend  fun deleteGenre(genre: GenreSerie)

    @Query("SELECT * FROM genres_serie")
    suspend  fun getGenres():Array<GenreSerie>
}