package com.jj.pelismtv.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jj.pelismtv.model.Player

@Dao
interface PlayerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend  fun insertPlayer(player: Player)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend  fun insertAllPlayer(player: List<Player>)

    @Update
    suspend  fun updatePlayer(player: Player)

    @Delete
    suspend  fun deletePlayer(player: Player)

    @Query("SELECT * FROM players where movie_id = :movie order by id desc")
    suspend fun getPlayer(movie:Int):Array<Player>

    @Query("SELECT * FROM players where movie_id = :movie and link LIKE '%drive%' and type = 'Download' LIMIT 1")
    suspend  fun getPlayerDownload(movie:Int):Player
}