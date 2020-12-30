package com.jj.pelismtv.domain

import com.jj.pelismtv.logic.SerieResource
import com.jj.pelismtv.model.*
import com.jj.pelismtv.vo.Resource
import io.reactivex.Flowable

class SerieUseCase() {
    private val serieSource = SerieResource()

    suspend fun getGenres(): Resource<Array<GenreSerie>> {
        return serieSource.getGenres()
    }

    suspend fun getSerie(id:Int): Resource<SerieWithGenres> {
        return serieSource.getSerie(id)
    }
    fun getSeasons(id:Int): Flowable<List<Season>> {
        return serieSource.getSeasons(id)
    }
    fun getEpisodes(id:Int): Flowable<List<Episode>> {
        return serieSource.getEpisodes(id)
    }

    suspend fun getVideoInfo(episode:Int): Resource<Array<PlayerSerie>> {
        return serieSource.getVideo(episode)
    }

    suspend fun getDataInfo(id:Int): Resource<Array<EpisodeSeason>> {
        return serieSource.getDataInfo(id)
    }

    suspend fun otherEpisode(season_id:Int,number:Int): Resource<Episode>{
        return serieSource.otherEpisode(season_id,number)
    }

    fun getGenresWithSeries(): Flowable<List<GenreWithSeries>> {
        return serieSource.getListGenreSeries()
    }

}