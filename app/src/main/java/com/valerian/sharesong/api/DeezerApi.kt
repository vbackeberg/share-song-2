package com.valerian.sharesong.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DeezerApi {
    @GET("search")
    fun getTrackBySearch(@Query("q") searchTerm: String): Call<DeezerSearchResults>

    @GET("track/{id}")
    fun getTrackById(@Path("id") id: String): Call<DeezerTrack>

    @GET("https://deezer.page.link/{page}")
    fun getTrackIdByPage(@Path("page") page: String): Call<ResponseBody>
}

data class DeezerTrack(val title: String, val artist: Artist) {
    data class Artist(
        val name: String
    )
}

data class DeezerSearchResults(
    val data: List<Data>
) {
    data class Data(
        val link: String
    )
}

object DeezerClient {
    val instance = createDeezerApi()

    private fun createDeezerApi(): DeezerApi = Retrofit.Builder()
        .baseUrl("https://api.deezer.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(DeezerApi::class.java)
}