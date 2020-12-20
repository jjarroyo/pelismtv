package com.jj.pelismtv.ui.splash

import android.accessibilityservice.GestureDescription
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.ViewModel
import com.jj.pelismtv.AppDelegate
import com.jj.pelismtv.logic.*
import com.jj.pelismtv.utils.ThreadManager


class SplashScreenViewModel : ViewModel(){

    private val movieSource = MovieResource()
    private val genreSource = GenreResource()
    private val movieGenreSource = MovieGenreResource()
    private val serieResource = SerieResource()
    private val genreserieResource = GenreSerieResource()
    private val seasonResource = SeasonResource()
    private val episodeResource = EpisodeResource()

    suspend fun importData(text_status: TextView?) {
        updateStatus(text_status, "Sincronizando peliculas...")
        Log.e("ini","ini")
        genreSource.importGenre()
        Log.e("ini","genres")
        movieSource.importMovie()
        Log.e("ini","movies")
        movieGenreSource.importMovieGenre()
        Log.e("ini","m genres")
        updateStatus(text_status, "Sincronizando series...")
        serieResource.importSerie()
        genreserieResource.importGenreSerie()
        genreserieResource.importGenreSerieTv()
        seasonResource.importSeasonSerie()
        episodeResource.importEpisodeSerie()
        updateStatus(text_status,"Finalizando...")
    }

    fun updateStatus(text_status: TextView?,value:String){
        ThreadManager.executeInMainThread {
            text_status?.text = value
        }
    }
}