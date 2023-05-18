package com.valerian.sharesong.converter.from

import android.net.Uri
import com.valerian.sharesong.api.DeezerClient
import com.valerian.sharesong.converter.Track
import com.valerian.sharesong.countryCodeFromHtml
import retrofit2.await

class Deezer : FromService {
    override suspend fun getTrack(intentUri: Uri): Track? {
        val page = intentUri.lastPathSegment.toString()
        val siteAsString =
            DeezerClient.instance.getTrackIdByPage(page).await().string()
        val countryCode = countryCodeFromHtml(siteAsString)

        if (countryCode == null) {
            println("Could not find a countryCode.")
            return null
        }

        val trackId =
            Uri.parse(getRegex(countryCode).find(siteAsString)?.value).lastPathSegment
                ?: return null

        val track = DeezerClient.instance.getTrackById(trackId).await()

        return Track(track.title, track.artist.name)
    }

    companion object {
        private fun getRegex(countryCode: String) =
            "https://www\\.deezer\\.com/${countryCode}/track/(\\d+)".toRegex()
    }
}