package com.jj.pelismtv.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jj.pelismtv.domain.SerieUseCase

class SerieViewModelFactory(private val serieUseCase: SerieUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(SerieUseCase::class.java).newInstance(serieUseCase)
    }
}