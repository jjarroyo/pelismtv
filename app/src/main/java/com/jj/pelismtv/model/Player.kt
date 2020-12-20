package com.jj.pelismtv.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Entity(tableName = "players",indices = [Index(value = ["link"], unique = true)])
data class Player(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val movie_id:Int,
    val link:String,
    val type:String? = ""
)
val MIGRATION_1_2  = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE Players ADD COLUMN type TEXT DEFAULT ''")
    }
}
val MIGRATION_2_3  = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_players_link ON players (link)")
    }
}