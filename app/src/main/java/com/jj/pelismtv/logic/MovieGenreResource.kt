package com.jj.pelismtv.logic

import android.util.Log
import com.google.gson.annotations.SerializedName
import com.jj.pelismtv.AppDelegate
import com.jj.pelismtv.model.MovieGenre
import com.jj.pelismtv.model.Setting
import com.jj.pelismtv.utils.RetrofitClient
import org.json.JSONObject

class MovieGenreResource {
    private val animegenredao = AppDelegate.database!!.movieGenreDao()
    private val  settingdao = AppDelegate.database!!.settingDao()

    private var count = 1
    suspend fun importMovieGenre() {
        try {
            val setting = settingdao.geSetting("filter-moviegenre")
            var filter:String? = null
            val sort = JSONObject(mapOf("updated_at" to "ASC")).toString()
            if(setting != null){
                val array = mapOf("modified" to "${setting.value}")
                val jsonArray = JSONObject(array)
                filter = jsonArray.toString()
            }

            val data = RetrofitClient.retrofitService.getMovieGenres(sort,filter,count)
            if(data.movieGenreList.count() > 0){
                animegenredao.insertAllMovieGenre(data.movieGenreList)
                Log.e("test", data.current_page.toString())
                if(data.current_page < data.last_page){
                    count = data.current_page + 1
                    importMovieGenre()
                }else{
                    val newSetting = Setting("filter-moviegenre",data.movieGenreList.last().updated_at)
                    settingdao.insertSetting(newSetting)
                }
            }
        }catch(exception: Exception){
            Log.e("error relation", exception.message.toString())
        }
    }


}



data class DataMovieGenre(
    @SerializedName("data")
    val movieGenreList:List<MovieGenre>  = listOf(),
    val current_page:Int,
    val last_page:Int
)