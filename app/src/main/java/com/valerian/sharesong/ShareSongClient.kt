package com.valerian.sharesong

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ShareSongApi {
    @GET("convert")
    fun convert(
        @Query("originServiceUrl") originServiceUrl: String,
        @Query("targetServiceName") targetService: String
    ): Call<ResponseBody>
}

object ShareSongClient {
    val instance = createShareSongApi()

    private fun createShareSongApi(): ShareSongApi {
        return Retrofit.Builder()
            .baseUrl("https://firebase/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ShareSongApi::class.java)
    }
}