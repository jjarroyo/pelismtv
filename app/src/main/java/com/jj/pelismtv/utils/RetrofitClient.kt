package com.jj.pelismtv.utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {


    val BASE_URL = "https://pelismayo.com/"
   // val BASE_URL = "http://192.168.1.6:8000"
    val retrofitService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(RetrofitService::class.java)
    }

}