package com.jj.pelismtv.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jj.pelismtv.domain.MovieUseCase

class MainViewModelFactory(private val movieUseCase: MovieUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(MovieUseCase::class.java).newInstance(movieUseCase)
    }
}