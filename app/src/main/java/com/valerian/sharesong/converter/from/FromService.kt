package com.valerian.sharesong.converter.from

import android.net.Uri
import com.valerian.sharesong.converter.Track

interface FromService {
    suspend fun getTrack(intentUri: Uri): Track?
}

suspend fun convertFrom(intentUri: Uri): Track? {
    val originService = when (intentUri.host) {
        "open.spotify.com" -> Spotify()
        "deezer.page.link" -> Deezer()
        else -> {
            print("intentUri $intentUri does not match any host")
            return null
        }
    }
    println(intentUri)

    val track = originService.getTrack(intentUri)
    if (track == null) {
        print("no track found")
        return null
    }

    println(track)
    return track
}