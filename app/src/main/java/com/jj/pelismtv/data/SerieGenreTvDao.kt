package com.jj.pelismtv.data

import androidx.room.*
import com.jj.pelismtv.model.MovieGenre
import com.jj.pelismtv.model.SerieGenre

@Dao
interface SerieGenreTvDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend  fun insertSerieGenre(serieGenre: SerieGenre)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend  fun insertAllSerieGenre(moviegenre: List<SerieGenre>)

    @Update
    suspend  fun updateSerieGenre(serieGenre: SerieGenre)

    @Delete
    suspend  fun deleteSerieGenre(serieGenre: SerieGenre)
}