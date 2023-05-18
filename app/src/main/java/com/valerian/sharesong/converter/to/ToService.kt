package com.valerian.sharesong.converter.to

import com.valerian.sharesong.converter.Track

interface ToService {
    suspend fun getTrackUrl(track: Track): String?
}

suspend fun convertTo(track: Track, targetService: ToService): String? {
    val targetServiceUrl = targetService.getTrackUrl(track)

    if (targetServiceUrl == null) {
        println("Could not get target service URL")
        return null
    }
    println(targetServiceUrl)
    return targetServiceUrl
}