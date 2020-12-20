package com.jj.pelismtv.data

import androidx.room.*
import com.jj.pelismtv.model.Season


@Dao
interface SeasonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeason(season: Season)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSeason(season: List<Season>)

    @Update
    suspend fun updateSeason(season: Season)

    @Delete
    suspend fun deleteSeason(season: Season)

    @Query("SELECT * FROM seasons WHERE serie_id = :id")
    suspend fun getSeasons(id:Int):  Array<Season>

}