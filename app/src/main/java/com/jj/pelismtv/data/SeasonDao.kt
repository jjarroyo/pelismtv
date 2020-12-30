package com.jj.pelismtv.data

import androidx.room.*
import com.jj.pelismtv.model.Season
import io.reactivex.Flowable


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
     fun getSeasons(id:Int): Flowable<List<Season>>

}