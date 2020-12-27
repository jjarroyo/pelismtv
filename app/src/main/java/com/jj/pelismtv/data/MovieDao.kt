package com.jj.pelismtv.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jj.pelismtv.model.GenreWithMovies
import com.jj.pelismtv.model.Movie
import com.jj.pelismtv.model.MovieWithGenres
import io.reactivex.Flowable

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend  fun insertMovie(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend  fun insertAllMovie(movie: List<Movie>)

    @Update
    suspend  fun updateMovie(movie: Movie)

    @Delete
    suspend  fun deleteMovie(movie: Movie)

    @Query("SELECT * FROM movies where title LIKE '%' || :search || '%'" )
    suspend fun getMovies(search:String?): Array<Movie>



    @Query("SELECT * FROM movies where title LIKE '%' || :search || '%'")
    fun getMovie(search :String): LiveData<List<Movie>>

    @Query("SELECT * FROM movies where id = :id  ")
    fun getMovieSelect(id :Int): LiveData<Movie>


    @Query("SELECT m.* FROM movies as m inner join movie_genre as mg on mg.movie_id = m.id inner join genres as g on g.id = mg.genre_id where m.title LIKE '%' || :search || '%' and  g.label in (:genres)  and  m.release_date BETWEEN  :startYear  AND :endYear order by m.id desc " )
    suspend fun getMoviesRecent(search:String?,startYear:Long?,endYear:Long?,genres:Array<String>?): Array<Movie>

    @Query("SELECT m.* FROM movies as m inner join movie_genre as mg on mg.movie_id = m.id inner join genres as g on g.id = mg.genre_id where m.title LIKE '%' || :search || '%' and  g.label in (:genres)  and  m.release_date BETWEEN  :startYear  AND :endYear order by m.id asc " )
    suspend fun getMoviesOld(search:String?,startYear:Long?,endYear:Long?,genres:Array<String>?): Array<Movie>

    @Query("SELECT m.* FROM movies as m inner join movie_genre as mg on mg.movie_id = m.id inner join genres as g on g.id = mg.genre_id where m.title LIKE '%' || :search || '%' and  g.label in (:genres)  and  m.release_date BETWEEN  :startYear  AND :endYear order by m.release_date desc " )
    suspend fun getMoviesRecentYear(search:String?,startYear:Long?,endYear:Long?,genres:Array<String>?): Array<Movie>

    @Query("SELECT m.* FROM movies as m inner join movie_genre as mg on mg.movie_id = m.id inner join genres as g on g.id = mg.genre_id where m.title LIKE '%' || :search || '%' and  g.label in (:genres)  and  m.release_date BETWEEN  :startYear  AND :endYear order by m.release_date asc " )
    suspend fun getMoviesOldYear(search:String?,startYear:Long?,endYear:Long?,genres:Array<String>?): Array<Movie>


    @Query("SELECT * FROM movies order by id desc limit :col ")
    fun getMoviesRecent(col: Int): LiveData<List<Movie>>

    @Query("SELECT * FROM genres")
    fun getMovieGenres(): Flowable<List<GenreWithMovies>>


    @Query("SELECT m.* FROM movies  as m inner join movie_genre as mg on mg.movie_id = m.id inner join genres as g on g.id = mg.genre_id where g.id == :genre  order by id desc limit :col ")
    fun getMoviesForGenre(col: Int,genre:Int):  LiveData<List<Movie>>

    @Query("SELECT * FROM movies where title LIKE '%' || :search || '%' and release_date BETWEEN  :startYear  AND :endYear order by release_date desc ")
    suspend fun getMoviesRecentYear(search:String?,startYear:Long?,endYear:Long?): Array<Movie>
    @Query("SELECT * FROM movies where title LIKE '%' || :search || '%' and release_date BETWEEN  :startYear  AND :endYear order by release_date asc ")
    suspend fun getMoviesOldYear(search:String?,startYear:Long?,endYear:Long?): Array<Movie>

}