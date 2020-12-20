package com.jj.pelismtv.logic

import android.util.Log
import com.google.gson.annotations.SerializedName
import com.jj.pelismtv.AppDelegate
import com.jj.pelismtv.model.Episode
import com.jj.pelismtv.model.Season
import com.jj.pelismtv.model.Setting
import com.jj.pelismtv.utils.RetrofitClient
import org.json.JSONObject

class EpisodeResource {
    private val  settingdao = AppDelegate.database!!.settingDao()
    private val episodedao = AppDelegate.database!!.episodeDao()
    private var count = 1
    suspend fun importEpisodeSerie() {
        try {
            val setting = settingdao.geSetting("filter-episode")
            var filter:String? = null
            val sort = JSONObject(mapOf("updated_at" to "ASC")).toString()

            if(setting != null){
                val array = mapOf("modified" to "${setting.value}")
                val jsonArray = JSONObject(array)
                filter = jsonArray.toString()
            }

            val data = RetrofitClient.retrofitService.getEpisodes(sort,filter,count)
            Log.e("sincronizacion","episode"+data.episodeList.count())
            if(data.episodeList.count() > 0){
                episodedao.insertAllEpisode(data.episodeList)
                if(data.current_page < data.last_page){
                    count = data.current_page + 1
                    importEpisodeSerie()
                }else{
                    val newSetting = Setting("filter-episode",data.episodeList.last().updated_at)
                    settingdao.insertSetting(newSetting)
                }
            }
        }catch(exception: Exception){
            Log.e("error episode", exception.message.toString())
        }
    }


}

data class DataEpisode(
    @SerializedName("data")
    val episodeList:List<Episode>  = listOf(),
    val current_page:Int,
    val last_page:Int
)