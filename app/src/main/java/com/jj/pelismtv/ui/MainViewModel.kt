package com.jj.pelismtv.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.jj.pelismtv.domain.MovieUseCase
import com.jj.pelismtv.model.Movie

class MainViewModel(val movieUseCase: MovieUseCase) : ViewModel() {

    private var movieLiveData: LiveData<List<Movie>>? = null
    fun getMovies(): LiveData<List<Movie>>? {
        if (movieLiveData == null) {
            movieLiveData = movieUseCase.getList()
        }
        return movieLiveData
    }

}