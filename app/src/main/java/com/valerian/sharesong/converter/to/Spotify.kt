package com.valerian.sharesong.converter.to

import com.valerian.sharesong.api.SpotifyAccessTokenProvider
import com.valerian.sharesong.api.SpotifyClient
import com.valerian.sharesong.converter.Track
import retrofit2.await

class Spotify : ToService {
    override suspend fun getTrackUrl(track: Track): String? {
        val accessTokenResponse = SpotifyAccessTokenProvider.get()
        val searchTerm = "${track.title} ${track.artist.orEmpty()}"
        return SpotifyClient.instance.getTrackBySearch(searchTerm,
            "track",
            "Bearer ${accessTokenResponse.accessToken}")
            .await().tracks.items.firstOrNull()?.externalUrls?.spotify
    }
}
