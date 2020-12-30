package com.jj.pelismtv.logic

import android.util.Log
import com.google.gson.annotations.SerializedName
import com.jj.pelismtv.AppDelegate
import com.jj.pelismtv.model.*
import com.jj.pelismtv.utils.RetrofitClient
import com.jj.pelismtv.vo.Resource
import io.reactivex.Flowable
import org.json.JSONObject

class SerieResource {
    private var serieArrayList = arrayOf<Serie>()
    private val  settingdao = AppDelegate.database!!.settingDao()
    private val seriedao = AppDelegate.database!!.serieDao()
    private val genredao = AppDelegate.database!!.genreTvDao()
    private val seasondao = AppDelegate.database!!.seasonDao()
    private val episodedao = AppDelegate.database!!.episodeDao()
    private val playerseriedao = AppDelegate.database!!.playerSerieDao()

    fun getListGenreSeries(): Flowable<List<GenreWithSeries>> {

        return seriedao.getListGenreSeries()
    }

    suspend fun getGenres(): Resource<Array<GenreSerie>> {
        return Resource.Success(genredao.getGenres())
    }

    suspend fun getSerie(id:Int): Resource<SerieWithGenres> {
        Log.e("llega","sdaaaaasdsd")
        var serie = seriedao.getSerie(id)
        return Resource.Success(serie)
    }

    fun getSeasons(id:Int): Flowable<List<Season>> {
        Log.e("llega","sdaaaaasdsd")
        val serie = seasondao.getSeasons(id)
        return serie
    }

    fun getEpisodes(id:Int):Flowable<List<Episode>> {
        Log.e("llega","sdaaaaasdsd")
        return episodedao.getEpisodes(id)

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