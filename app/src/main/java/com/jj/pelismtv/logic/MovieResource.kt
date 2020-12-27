package com.jj.pelismtv.logic

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.gson.annotations.SerializedName
import com.jj.pelismtv.AppDelegate
import com.jj.pelismtv.model.*
import com.jj.pelismtv.utils.RetrofitClient
import com.jj.pelismtv.vo.Resource
import io.reactivex.Flowable
import org.json.JSONObject

class MovieResource {
    private var movieArrayList = arrayOf<Movie>()
    private val moviedao = AppDelegate.database!!.movieDao()
    private val  settingdao = AppDelegate.database!!.settingDao()
    private val playerdao = AppDelegate.database!!.playerDao()
    private val genredao = AppDelegate.database!!.genreDao()

    fun getListMovies(col: Int): LiveData<List<Movie>> {

        return moviedao.getMoviesRecent(col)
    }
    fun getListMoviesForGenre(col: Int,genre: Int): LiveData<List<Movie>> {

        return moviedao.getMoviesForGenre(col,genre)
    }

    fun getListMovieGenres(): Flowable<List<GenreWithMovies>> {

        return moviedao.getMovieGenres()
    }


    private var count = 1
    suspend fun importMovie() {
        try {
            val setting = settingdao.geSetting("filter-anime")
            var filter:String? = null
            val sort = JSONObject(mapOf("updated_at" to "ASC")).toString()

            if(setting != null){
                val array = mapOf("modified" to "${setting.value}")
                val jsonArray = JSONObject(array)
                filter = jsonArray.toString()
            }

            val data = RetrofitClient.retrofitService.getMovies(sort,filter,count)
            if(data.movieList.count() > 0){
                moviedao.insertAllMovie(data.movieList)
                if(data.current_page < data.last_page){
                    count = data.current_page + 1
                    importMovie()
                }else{
                    val newSetting = Setting("filter-anime",data.movieList.last().updated_at)
                    settingdao.insertSetting(newSetting)
                }
            }
        }catch(exception: Exception){
            Log.e("error movie", exception.message.toString())
        }
    }


    suspend fun getVideo(movie:Int): Resource<Array<Player>> {
        importPlayer(movie)
        return Resource.Success(playerdao.getPlayer(movie))
    }

    suspend fun getVideoDownload(movie:Int): Resource<Player> {
        var download = playerdao.getPlayerDownload(movie)
        if (download == null){
            importPlayer(movie)
            download = playerdao.getPlayerDownload(movie)
        }
        return Resource.Success(download)
    }


    private suspend fun importPlayer(movie:Int) {
        try {

            val array = mapOf("movie" to "$movie")
            val jsonArray = JSONObject(array)
            val filter = jsonArray.toString()

            val data = RetrofitClient.retrofitService.getPlayers(filter)

            if(data.players.count() > 0){
                Log.e("players",data.players.toString())
                playerdao.insertAllPlayer(data.players)
            }

        }catch(exception: Exception){
            Log.e("error movie", exception.message.toString())
        }
    }

    fun getMovie(search :String): LiveData<List<Movie>> {
        return moviedao.getMovie(search)

    }

    fun getMovieSelected(id :Int): LiveData<Movie> {
        return moviedao.getMovieSelect(id)

    }

    suspend fun getGenres(): Resource<Array<Genre>> {
        return Resource.Success(genredao.getGenres())
    }

    suspend fun sendData(body: String):Boolean{
      return  RetrofitClient.retrofitService.sendData(body)
    }

}

data class DataMovie(
    @SerializedName("data")
    val movieList:List<Movie>  = listOf(),
    val current_page:Int,
    val last_page:Int
)

data class DataPlayer(
    @SerializedName("data")
    val players:List<Player>  = listOf()
)