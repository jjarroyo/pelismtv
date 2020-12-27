package com.jj.pelismtv.domain

import androidx.lifecycle.LiveData
import com.jj.pelismtv.logic.MovieResource
import com.jj.pelismtv.model.*
import com.jj.pelismtv.vo.Resource
import io.reactivex.Flowable

class MovieUseCase() {

    private val movieSource = MovieResource()

    fun getList(col: Int): LiveData<List<Movie>> {
        return movieSource.getListMovies(col)
    }
    fun getListForGenre(col: Int,genre: Int): LiveData<List<Movie>> {
        return movieSource.getListMoviesForGenre(col,genre)
    }

    fun getSearchMovie(search :String): LiveData<List<Movie>> {
        return movieSource.getMovie(search)
    }

    fun getMovie(id: Int): LiveData<Movie> {
        return movieSource.getMovieSelected(id)
    }

    suspend fun getVideoInfo(movie:Int): Resource<Array<Player>> {
        return movieSource.getVideo(movie)
    }

    suspend fun getVideoDownload(movie:Int): Resource<Player> {
        return movieSource.getVideoDownload(movie)
    }

    fun getGenresWithMovies(): Flowable<List<GenreWithMovies>> {
        return movieSource.getListMovieGenres()
    }

    suspend fun sendData(body: String):Boolean{
        return movieSource.sendData(body)
    }
}