package com.jj.pelismtv.data

import androidx.room.*
import com.jj.pelismtv.model.Episode
import com.jj.pelismtv.model.EpisodeSeason
import com.jj.pelismtv.model.Season
import io.reactivex.Flowable

@Dao
interface EpisodeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpisode(episode: Episode)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllEpisode(episode: List<Episode>)

    @Update
    suspend fun updateEpisode(episode: Episode)

    @Delete
    suspend fun deleteEpisode(episode: Episode)

    @Query("SELECT * FROM episodes WHERE season_id = :id")
    fun getEpisodes(id:Int): Flowable<List<Episode>>

    @Query("SELECT * FROM episodes WHERE id = :id LIMIT 1")
    suspend fun getDataInfo(id:Int): Array<EpisodeSeason>

    @Query("SELECT * FROM episodes WHERE season_id =:season_id and episode_number = :number LIMIT 1")
    suspend fun otherEpisode(season_id:Int,number:Int): Episode
}
