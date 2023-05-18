package com.valerian.sharesong.api

import com.valerian.sharesong.Secrets
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.FormBody
import org.junit.Test
import retrofit2.await

class SpotifyClientTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getAccessToken() = runTest {
        val instance = SpotifyClient.instance

        val accessToken =
            instance.getAccessToken(FormBody.Builder()
                .add("grant_type", "client_credentials")
                .add("client_id",
                    Secrets().getclientId4("com.valerian.sharesong"))
                .add("client_secret",
                    Secrets().getclientSecret4("com.valerian.sharesong"))
                .build()).await()

        assert(accessToken.accessToken.isNotBlank())
    }
}