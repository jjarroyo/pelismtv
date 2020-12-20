package com.jj.pelismtv.model

import androidx.room.Entity
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.*


@Entity(tableName = "movies", primaryKeys = ["id"])
data class Movie(
    val id: Int,
    val title: String,
    val overview: String? = null,
    @TypeConverters(DateConverter::class)
    var release_date: Date,
    val poster_path: String,
    val trailer: String? = null,
    val updated_at: String
)

val MIGRATION_3_4: Migration = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE movies "
                    + " ALTER COLUMN release_date DATE"
        )
    }
}