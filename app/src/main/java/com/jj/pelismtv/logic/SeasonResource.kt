package com.jj.pelismtv.logic

import android.util.Log
import com.google.gson.annotations.SerializedName
import com.jj.pelismtv.AppDelegate
import com.jj.pelismtv.model.Season
import com.jj.pelismtv.model.Setting
import com.jj.pelismtv.utils.RetrofitClient
import org.json.JSONObject

class SeasonResource {

    private val  settingdao = AppDelegate.database!!.settingDao()
    private val seasondao = AppDelegate.database!!.seasonDao()
    private var count = 1
    suspend fun importSeasonSerie() {
        try {
            val setting = settingdao.geSetting("filter-season")
            var filter:String? = null
            val sort = JSONObject(mapOf("updated_at" to "ASC")).toString()

            if(setting != null){
                val array = mapOf("modified" to "${setting.value}")
                val jsonArray = JSONObject(array)
                filter = jsonArray.toString()
            }

            val data = RetrofitClient.retrofitService.getSeasons(sort,filter,count)
            Log.e("sincronizacion","season"+data.seasonList.count())
            if(data.seasonList.count() > 0){
                seasondao.insertAllSeason(data.seasonList)
                if(data.current_page < data.last_page){
                    count = data.current_page + 1
                    importSeasonSerie()
                }else{
                    val newSetting = Setting("filter-season",data.seasonList.last().updated_at)
                    settingdao.insertSetting(newSetting)
                }
            }
        }catch(exception: Exception){
            Log.e("errorseason", exception.message.toString())
        }
    }


}

data class DataSeason(
    @SerializedName("data")
    val seasonList:List<Season>  = listOf(),
    val current_page:Int,
    val last_page:Int
)