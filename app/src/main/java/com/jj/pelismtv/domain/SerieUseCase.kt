package com.jj.pelismtv.domain

import com.jj.pelismtv.logic.SerieResource
import com.jj.pelismtv.model.*
import com.jj.pelismtv.vo.Resource

class SerieUseCase() {
    private val serieSource = SerieResource()

    suspend fun getList(sort:Int,search:String?,startYear:Long?,endYear:Long?,genres:Array<String>? = null): Resource<Array<Serie>> {
        return serieSource.getListSeries(sort,search,startYear,endYear,genres)
    }

    suspend fun getGenres(): Resource<Array<GenreSerie>> {
        return serieSource.getGenres()
    }

    suspend fun getSerie(id:Int): Resource<SerieWithGenres> {
        return serieSource.getSerie(id)
    }
    suspend fun getSeasons(id:Int): Resource<Array<Season>> {
        return serieSource.getSeasons(id)
    }
    suspend fun getEpisodes(id:Int): Resource<Array<Episode>> {
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

}