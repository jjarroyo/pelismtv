package com.jj.pelismtv.data

import androidx.room.*
import com.jj.pelismtv.model.PlayerSerie
@Dao
interface PlayerSerieDao   {

@Insert(onConflict = OnConflictStrategy.REPLACE)
suspend  fun insertPlayerSerie(player: PlayerSerie)

@Insert(onConflict = OnConflictStrategy.REPLACE)
suspend  fun insertAllPlayerSerie(player: List<PlayerSerie>)

@Update
suspend  fun updatePlayerSerie(player: PlayerSerie)

@Delete
suspend  fun deletePlayerSerie(player: PlayerSerie)

@Query("SELECT * FROM players_serie where episode_id = :episode order by id desc")
suspend  fun getPlayerSerie(episode:Int):Array<PlayerSerie>

}