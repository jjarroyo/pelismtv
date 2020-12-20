package com.jj.pelismtv.logic

import android.util.Log
import com.google.gson.annotations.SerializedName
import com.jj.pelismtv.AppDelegate
import com.jj.pelismtv.model.*
import com.jj.pelismtv.utils.RetrofitClient
import com.jj.pelismtv.vo.Resource
import org.json.JSONObject

class SerieResource {
    private var serieArrayList = arrayOf<Serie>()
    private val  settingdao = AppDelegate.database!!.settingDao()
    private val seriedao = AppDelegate.database!!.serieDao()
    private val genredao = AppDelegate.database!!.genreTvDao()
    private val seasondao = AppDelegate.database!!.seasonDao()
    private val episodedao = AppDelegate.database!!.episodeDao()
    private val playerseriedao = AppDelegate.database!!.playerSerieDao()

    suspend fun getListSeries(sort:Int,search:String?,startYear:Long?,endYear:Long?,genres:Array<String>? = null): Resource<Array<Serie>> {
        if((genres?.size ?: 0 ) > 0){
            when (sort) {
                1 -> {
                    serieArrayList = seriedao.getSeriesRecent(search,startYear,endYear,genres)
                }
                2 -> {
                    serieArrayList = seriedao.getSeriesOld(search,startYear,endYear,genres)
                }
                3 -> {
                    serieArrayList = seriedao.getSeriesRecentYear(search,startYear,endYear,genres)
                }
                4 -> {
                    serieArrayList = seriedao.getSeriesOldYear(search,startYear,endYear,genres)
                }
            }
        }else{
            when (sort) {
                1 -> {
                    serieArrayList = seriedao.getSeriesRecent(search,startYear,endYear)
                }
                2 -> {
                    serieArrayList = seriedao.getSeriesOld(search,startYear,endYear)
                }
                3 -> {
                    serieArrayList = seriedao.getSeriesRecentYear(search,startYear,endYear)
                }
                4 -> {
                    serieArrayList = seriedao.getSeriesOldYear(search,startYear,endYear)
                }
            }
        }

        return Resource.Success(serieArrayList)
    }


    suspend fun getGenres(): Resource<Array<GenreSerie>> {
        return Resource.Success(genredao.getGenres())
    }

    suspend fun getSerie(id:Int): Resource<SerieWithGenres> {
        Log.e("llega","sdaaaaasdsd")
        var serie = seriedao.getSerie(id)
        return Resource.Success(serie)
    }

    suspend fun getSeasons(id:Int): Resource<Array<Season>> {
        Log.e("llega","sdaaaaasdsd")
        var serie = seasondao.getSeasons(id)
        return Resource.Success(serie)
    }

    suspend fun getEpisodes(id:Int): Resource<Array<Episode>> {
        Log.e("llega","sdaaaaasdsd")
        var serie = episodedao.getEpisodes(id)
        return Resource.Success(serie)
    }

    private var count = 1
    suspend fun importSerie() {
        try {
            val setting = settingdao.geSetting("filter-serie")
            var filter:String? = null
            val sort = JSONObject(mapOf("updated_at" to "ASC")).toString()

            if(setting != null){
                val array = mapOf("modified" to "${setting.value}")
                val jsonArray = JSONObject(array)
                filter = jsonArray.toString()
            }

            val data = RetrofitClient.retrofitService.getSeries(sort,filter,count)
            Log.e("sincronizacion","serie"+data.serieList.count())
            if(data.serieList.count() > 0){
                seriedao.insertAllSerie(data.serieList)
                if(data.current_page < data.last_page){
                    count = data.current_page + 1
                    importSerie()
                }else{
                    val newSetting = Setting("filter-serie",data.serieList.last().updated_at)
                    settingdao.insertSetting(newSetting)
                }
            }
        }catch(exception: Exception){
            Log.e("error serie", exception.message.toString())
        }
    }

    suspend fun getVideo(episode:Int): Resource<Array<PlayerSerie>> {
        importPlayer(episode)
        return Resource.Success(playerseriedao.getPlayerSerie(episode))
    }

    suspend fun getDataInfo(id:Int): Resource<Array<EpisodeSeason>> {
        return Resource.Success(episodedao.getDataInfo(id))
    }

    suspend fun otherEpisode(season_id:Int,number:Int): Resource<Episode>{
        val episode = episodedao.otherEpisode(season_id,number)
        return  Resource.Success(episode)
    }

    private suspend fun importPlayer(episode:Int) {
        try {
            val array = mapOf("episode" to "$episode")
            val jsonArray = JSONObject(array)
            val filter = jsonArray.toString()

            val data = RetrofitClient.retrofitService.getPlayersEpisode(filter)
            Log.e("se 0",data.toString())
            if(data.player_episode.count() > 0){

                playerseriedao.insertAllPlayerSerie(data.player_episode)
            }

        }catch(exception: Exception){
            Log.e("error movie", exception.message.toString())
        }
    }

}

data class DataSerie(
    @SerializedName("data")
    val serieList:List<Serie>  = listOf(),
    val current_page:Int,
    val last_page:Int
)


data class DataPlayerSerie(
    @SerializedName("data")
    val player_episode:List<PlayerSerie>  = listOf()
)