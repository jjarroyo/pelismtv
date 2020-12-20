package com.jj.pelismtv.utils


import com.jj.pelismtv.logic.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RetrofitService {


    @GET("api/genres")
    suspend fun getGenres(@Query("sort") sort: String? = "{'updated_at':'ASC'}",
                          @Query("filters") filters: String? =  "",
                          @Query("page") page: Int? = 1): DataGenre


    @GET("api/movies")
    suspend fun getMovies(@Query("sort") sort: String? = "{'updated_at':'ASC'}",
                @Query("filters") filters: String? =  "",
                @Query("page") page: Int? = 1): DataMovie

    @GET("api/movie_genre")
    suspend fun getMovieGenres(@Query("sort") sort: String? = "{'updated_at':'ASC'}",
                          @Query("filters") filters: String? =  "",
                          @Query("page") page: Int? = 1): DataMovieGenre


    @GET("api/player")
    suspend fun getPlayers( @Query("filters") filters: String? =  ""): DataPlayer


    @POST("api/report")
    suspend fun sendData(@Body data:String):Boolean

    @GET("api/series")
    suspend fun getSeries(@Query("sort") sort: String? = "{'updated_at':'ASC'}",
                          @Query("filters") filters: String? =  "",
                          @Query("page") page: Int? = 1): DataSerie


    @GET("api/genres_tv")
    suspend fun getGenresTv(@Query("sort") sort: String? = "{'updated_at':'ASC'}",
                          @Query("filters") filters: String? =  "",
                          @Query("page") page: Int? = 1): DataGenreSerie

    @GET("api/serie_genres")
    suspend fun getSerieGenres(@Query("sort") sort: String? = "{'updated_at':'ASC'}",
                          @Query("filters") filters: String? =  "",
                          @Query("page") page: Int? = 1): DatSerieaGenreTv

    @GET("api/seasons")
    suspend fun getSeasons(@Query("sort") sort: String? = "{'updated_at':'ASC'}",
                          @Query("filters") filters: String? =  "",
                          @Query("page") page: Int? = 1): DataSeason

    @GET("api/episodes")
    suspend fun getEpisodes(@Query("sort") sort: String? = "{'updated_at':'ASC'}",
                          @Query("filters") filters: String? =  "",
                          @Query("page") page: Int? = 1): DataEpisode

    @GET("api/player_episode")
    suspend fun getPlayersEpisode( @Query("filters") filters: String? =  ""): DataPlayerSerie
}