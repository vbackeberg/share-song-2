package com.valerian.sharesong.converter.from

import android.net.Uri
import com.valerian.sharesong.api.SpotifyAccessTokenProvider
import com.valerian.sharesong.api.SpotifyClient
import com.valerian.sharesong.converter.Track
import retrofit2.await

class Spotify : FromService {
    override suspend fun getTrack(intentUri: Uri): Track {
        val accessTokenResponse = SpotifyAccessTokenProvider.get()

        val trackId = intentUri.lastPathSegment.toString()

        val track =
            SpotifyClient.instance.getTrackById(trackId,
                "Bearer ${accessTokenResponse.accessToken}").await()

        return Track(track.name, track.artists.firstOrNull()?.name)
    }
}