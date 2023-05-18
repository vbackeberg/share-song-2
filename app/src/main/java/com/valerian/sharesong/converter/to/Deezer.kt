package com.valerian.sharesong.converter.to

import com.valerian.sharesong.api.DeezerClient
import com.valerian.sharesong.converter.Track
import retrofit2.await

class Deezer : ToService {
    override suspend fun getTrackUrl(track: Track): String? {
        val searchTerm = "track:\"${track.title}\" artist:\"${track.artist}\""

        return DeezerClient.instance.getTrackBySearch(searchTerm)
            .await().data.firstOrNull()?.link
    }
}