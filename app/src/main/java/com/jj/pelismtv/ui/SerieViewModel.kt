package com.jj.pelismtv.ui

import androidx.lifecycle.ViewModel
import com.jj.pelismtv.domain.SerieUseCase
import com.jj.pelismtv.model.*
import io.reactivex.Flowable

class SerieViewModel(val serieUseCase: SerieUseCase) : ViewModel() {
    var arrayPlayer:ArrayList<Player> = ArrayList()
    var arrayGenres:ArrayList<GenreSerie> = ArrayList()

    private var genreWithSerieSLiveData: Flowable<List<GenreWithSeries>>? = null
    fun getGenreWithSerie(): Flowable<List<GenreWithSeries>>? {
        genreWithSerieSLiveData = serieUseCase.getGenresWithSeries()
        //    }

        return genreWithSerieSLiveData
    }

    private var seasons: Flowable<List<Season>>? = null
    fun getSeasons(id: Int): Flowable<List<Season>>? {
        seasons = serieUseCase.getSeasons(id)
        //    }

        return seasons
    }

    private var episodes: Flowable<List<Episode>>? = null
    fun getEpisodes(id: Int): Flowable<List<Episode>>? {
        episodes = serieUseCase.getEpisodes(id)
        //    }

        return episodes
    }
}