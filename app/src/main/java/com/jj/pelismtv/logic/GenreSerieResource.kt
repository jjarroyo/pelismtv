package com.jj.pelismtv.logic

import android.util.Log
import com.google.gson.annotations.SerializedName
import com.jj.pelismtv.AppDelegate
import com.jj.pelismtv.model.GenreSerie
import com.jj.pelismtv.model.Serie
import com.jj.pelismtv.model.SerieGenre
import com.jj.pelismtv.model.Setting
import com.jj.pelismtv.utils.RetrofitClient
import org.json.JSONObject

class GenreSerieResource {

    private val  settingdao = AppDelegate.database!!.settingDao()
    private val genredao = AppDelegate.database!!.genreTvDao()
    private val genreseriedao = AppDelegate.database!!.genreSerieTv()
    private var count = 1
    suspend fun importGenreSerie() {
        try {
            val setting = settingdao.geSetting("filter-genre-serie")
            var filter:String? = null
            val sort = JSONObject(mapOf("updated_at" to "ASC")).toString()

            if(setting != null){
                val array = mapOf("modified" to "${setting.value}")
                val jsonArray = JSONObject(array)
                filter = jsonArray.toString()
            }

            val data = RetrofitClient.retrofitService.getGenresTv(sort,filter,count)
            Log.e("sincronizacion","serie genre"+data.genreList.count())
            if(data.genreList.count() > 0){
                genredao.insertAllGenre(data.genreList)
                if(data.current_page < data.last_page){
                    count = data.current_page + 1
                    importGenreSerie()
                }else{
                    val newSetting = Setting("filter-genre-serie",data.genreList.last().updated_at)
                    settingdao.insertSetting(newSetting)
                }
            }
        }catch(exception: Exception){
            Log.e("error genre serie", exception.message.toString())
        }
    }


    private var count2 = 1
    suspend fun importGenreSerieTv() {
        try {
            val setting = settingdao.geSetting("filter-genre-serie-tv")
            var filter:String? = null
            val sort = JSONObject(mapOf("updated_at" to "ASC")).toString()

            if(setting != null){
                val array = mapOf("modified" to "${setting.value}")
                val jsonArray = JSONObject(array)
                filter = jsonArray.toString()
            }

            val data = RetrofitClient.retrofitService.getSerieGenres(sort,filter,count2)
            Log.e("sincronizacion","serie genre tv"+data.genreSerieList.count())
            if(data.genreSerieList.count() > 0){
                genreseriedao.insertAllSerieGenre(data.genreSerieList)
                if(data.current_page < data.last_page){
                    count2 = data.current_page + 1
                    importGenreSerieTv()
                }else{
                    val newSetting = Setting("filter-genre-serie-tv",data.genreSerieList.last().updated_at)
                    settingdao.insertSetting(newSetting)
                }
            }
        }catch(exception: Exception){
            Log.e("error genre serie tv", exception.message.toString())
        }
    }


}

data class DataGenreSerie(
    @SerializedName("data")
    val genreList:List<GenreSerie>  = listOf(),
    val current_page:Int,
    val last_page:Int
)

data class DatSerieaGenreTv(
    @SerializedName("data")
    val genreSerieList:List<SerieGenre>  = listOf(),
    val current_page:Int,
    val last_page:Int
)