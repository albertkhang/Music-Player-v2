package com.albertkhang.app.networks

import com.albertkhang.app.utils.Songs
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class SongsService {
    interface SongInterface {
        @GET("5400939/top?limit=30")
        fun get(): Call<Songs>
    }
}