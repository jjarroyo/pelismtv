package com.jj.pelismtv.data

import androidx.room.*
import com.jj.pelismtv.model.Genre

@Dao
interface GenreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend  fun insertGenre(genre: Genre)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend  fun insertAllGenre(genre: List<Genre>)

    @Update
    suspend  fun updateGenre(genre: Genre)

    @Delete
    suspend  fun deleteGenre(genre: Genre)

    @Query("SELECT * FROM genres")
    suspend  fun getGenres():Array<Genre>
}