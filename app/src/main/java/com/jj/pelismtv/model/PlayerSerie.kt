package com.jj.pelismtv.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "players_serie")
data class PlayerSerie(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val episode_id:Int,
    val server:String
)