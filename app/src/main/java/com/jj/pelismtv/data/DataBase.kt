package com.jj.pelismtv.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jj.pelismtv.model.*


@Database(
    entities = [Movie::class, Genre::class, Player::class, MovieGenre::class,Setting::class,Serie::class,Season::class,Episode::class,GenreSerie::class,SerieGenre::class,PlayerSerie::class],version = 6
)
@TypeConverters(DateConverter::class)
abstract class DataBase : RoomDatabase(){

    abstract fun settingDao():SettingDao
    abstract fun movieDao():MovieDao
    abstract fun genreDao():GenreDao
    abstract fun playerDao():PlayerDao
    abstract fun movieGenreDao():MovieGenreDao
    abstract  fun serieDao():SerieDao
    abstract fun genreTvDao():GenreTvDao
    abstract fun seasonDao():SeasonDao
    abstract fun episodeDao():EpisodeDao
    abstract fun genreSerieTv():SerieGenreTvDao
    abstract fun playerSerieDao():PlayerSerieDao
}