package com.valerian.sharesong.api

import com.google.gson.annotations.SerializedName
import com.valerian.sharesong.Secrets
import okhttp3.FormBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SpotifyApi {
    @GET("v1/tracks/{id}")
    fun getTrackById(@Path("id") id: String,
        @Header("Authorization") bearerAccessToken: String
    ): Call<SpotifyTrack>

    @GET("v1/search")
    fun getTrackBySearch(@Query("q") searchTerm: String,
        @Query("type") type: String,
        @Header("Authorization") bearerAccessToken: String
    ): Call<SpotifySearchResults>

    @POST("https://accounts.spotify.com/api/token")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    fun getAccessToken(@Body credentials: RequestBody): Call<SpotifyAccessToken>
}

data class SpotifyTrack(val name: String, val artists: List<Artist>
) {
    data class Artist(val name: String
    )
}

data class SpotifySearchResults(val tracks: Tracks
) {
    data class Tracks(val items: List<Item>
    ) {
        data class Item(@SerializedName("external_urls") val externalUrls: ExternalUrls
        ) {
            data class ExternalUrls(val spotify: String
            )
        }
    }
}

data class SpotifyAccessToken(@SerializedName("access_token") val accessToken: String,
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("expires_in") val expiresIn: Int
)

object SpotifyClient {
    val instance = createSpotifyApi()

    private fun createSpotifyApi(): SpotifyApi {
        return Retrofit.Builder()
            .baseUrl("https://api.spotify.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SpotifyApi::class.java)
    }
}

object SpotifyAccessTokenProvider {
    private var expiresAt = 0L
    private var accessToken: SpotifyAccessToken? = null

    suspend fun get(): SpotifyAccessToken {
        if (accessToken !== null && expiresAt > System.currentTimeMillis()) return accessToken as SpotifyAccessToken

        val newAccessToken =
            SpotifyClient.instance.getAccessToken(FormBody.Builder()
                .add("grant_type", "client_credentials")
                .add("client_id",
                    Secrets().getclientId4("com.valerian.sharesong"))
                .add("client_secret",
                    Secrets().getclientSecret4("com.valerian.sharesong"))
                .build()).await()

        expiresAt = System.currentTimeMillis() + newAccessToken.expiresIn * 1000
        accessToken = newAccessToken
        return accessToken as SpotifyAccessToken
    }
}