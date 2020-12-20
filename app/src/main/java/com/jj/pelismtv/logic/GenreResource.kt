package com.jj.pelismtv.logic

import android.util.Log
import com.google.gson.annotations.SerializedName
import com.jj.pelismtv.AppDelegate
import com.jj.pelismtv.model.Genre
import com.jj.pelismtv.model.Setting
import com.jj.pelismtv.utils.RetrofitClient
import org.json.JSONObject

class GenreResource {

    private val genredao = AppDelegate.database!!.genreDao()
    private val  settingdao = AppDelegate.database!!.settingDao()

    suspend fun importGenre() {
        try {
            val setting = settingdao.geSetting("filter-genre")
            var filter:String? = null
            val sort = JSONObject(mapOf("updated_at" to "ASC")).toString()

            if(setting != null){
                val array = mapOf("modified" to "${setting.value}")
                val jsonArray = JSONObject(array)
                filter = jsonArray.toString()
            }

            val data = RetrofitClient.retrofitService.getGenres(sort,filter,1)
            if(data.genreList.count() > 0){
                genredao.insertAllGenre(data.genreList)
                if(data.current_page < data.last_page){
                    importGenre()
                }else{
                    val newSetting = Setting("filter-genre",data.genreList.last().updated_at)
                    settingdao.insertSetting(newSetting)
                }
            }
        }catch(exception: Exception){
            Log.e("error genre", exception.message.toString())
        }
    }

}


data class DataGenre(
    @SerializedName("data")
    val genreList:List<Genre>  = listOf(),
    val current_page:Int,
    val last_page:Int
)
