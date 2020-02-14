package com.albertkhang.app.networks

import com.albertkhang.app.utils.Songs
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Streaming

class SongsService {
    interface ApiInterface {
        @GET("artist/5400939/top?limit=30")
        fun get(): Call<Songs>
    }

    interface SongInterface {
        @GET("{fileName}")
        fun get(@Path("fileName") fileName: String): Call<ResponseBody>
    }
}