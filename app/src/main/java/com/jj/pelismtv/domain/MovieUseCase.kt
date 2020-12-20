package com.jj.pelismtv.domain

import androidx.lifecycle.LiveData
import com.jj.pelismtv.logic.MovieResource
import com.jj.pelismtv.model.Genre
import com.jj.pelismtv.model.Movie
import com.jj.pelismtv.model.MovieWithGenres
import com.jj.pelismtv.model.Player
import com.jj.pelismtv.vo.Resource

class MovieUseCase() {

    private val movieSource = MovieResource()

    fun getList(): LiveData<List<Movie>> {
        return movieSource.getListMovies()
    }


    suspend fun getMovie(id:Int): Resource<MovieWithGenres> {
        return movieSource.getMovie(id)
    }

    suspend fun getVideoInfo(movie:Int): Resource<Array<Player>> {
        return movieSource.getVideo(movie)
    }

    suspend fun getVideoDownload(movie:Int): Resource<Player> {
        return movieSource.getVideoDownload(movie)
    }

    suspend fun getGenres(): Resource<Array<Genre>> {
        return movieSource.getGenres()
    }

    suspend fun sendData(body: String):Boolean{
        return movieSource.sendData(body)
    }
}