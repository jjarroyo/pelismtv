package com.jj.pelismtv.data

import androidx.room.*
import com.jj.pelismtv.model.*
import io.reactivex.Flowable


@Dao
interface SerieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSerie(serie: Serie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSerie(serie: List<Serie>)

    @Update
    suspend fun updateSerie(serie: Serie)

    @Delete
    suspend fun deleteSerie(serie: Serie)

    @Query("SELECT * FROM series where id = :id")
    suspend fun getSerie(id:Int): SerieWithGenres


    @Query("SELECT s.* FROM series as s inner join serie_genre_tv as sgt on sgt.serie_id = s.id inner join genres_serie as gs on gs.id = sgt.genre_serie_id where s.title LIKE '%' || :search || '%' and  gs.label in (:genres)  and  s.first_air_date BETWEEN  :startYear  AND :endYear order by s.id desc " )
    suspend fun getSeriesRecent(search:String?,startYear:Long?,endYear:Long?,genres:Array<String>?): Array<Serie>

    @Query("SELECT s.* FROM series as s inner join serie_genre_tv as sgt on sgt.serie_id = s.id inner join genres_serie as gs on gs.id = sgt.genre_serie_id where s.title LIKE '%' || :search || '%' and  gs.label in (:genres)  and  s.first_air_date BETWEEN  :startYear  AND :endYear order by s.id asc " )
    suspend fun getSeriesOld(search:String?,startYear:Long?,endYear:Long?,genres:Array<String>?): Array<Serie>

    @Query("SELECT s.* FROM series as s inner join serie_genre_tv as sgt on sgt.serie_id = s.id inner join genres_serie as gs on gs.id = sgt.genre_serie_id where s.title LIKE '%' || :search || '%' and  gs.label in (:genres)  and  s.first_air_date BETWEEN  :startYear  AND :endYear order by s.first_air_date desc " )
    suspend fun getSeriesRecentYear(search:String?,startYear:Long?,endYear:Long?,genres:Array<String>?): Array<Serie>

    @Query("SELECT s.* FROM series as s inner join serie_genre_tv as sgt on sgt.serie_id = s.id inner join genres_serie as gs on gs.id = sgt.genre_serie_id where s.title LIKE '%' || :search || '%' and  gs.label in (:genres)  and  s.first_air_date BETWEEN  :startYear  AND :endYear order by s.first_air_date asc " )
    suspend fun getSeriesOldYear(search:String?,startYear:Long?,endYear:Long?,genres:Array<String>?): Array<Serie>


    @Query("SELECT * FROM series where title LIKE '%' || :search || '%' and first_air_date BETWEEN  :startYear  AND :endYear order by id desc ")
    suspend fun getSeriesRecent(search:String?,startYear:Long?,endYear:Long?): Array<Serie>
    @Query("SELECT * FROM series where title LIKE '%' || :search || '%' and first_air_date BETWEEN  :startYear  AND :endYear order by id asc ")
    suspend fun getSeriesOld(search:String?,startYear:Long?,endYear:Long?): Array<Serie>
    @Query("SELECT * FROM series where title LIKE '%' || :search || '%' and first_air_date BETWEEN  :startYear  AND :endYear order by first_air_date desc ")
    suspend fun getSeriesRecentYear(search:String?,startYear:Long?,endYear:Long?): Array<Serie>
    @Query("SELECT * FROM series where title LIKE '%' || :search || '%' and first_air_date BETWEEN  :startYear  AND :endYear order by first_air_date asc ")
    suspend fun getSeriesOldYear(search:String?,startYear:Long?,endYear:Long?): Array<Serie>


    @Query("SELECT * FROM genres_serie")
    fun getListGenreSeries(): Flowable<List<GenreWithSeries>>

}