package com.valerian.sharesong.converter.from

import android.net.Uri


object UriFromSharedString {
    fun get(string: String): String? {
        val uriString = when {
            string.contains("deezer.page.link") -> REGEX_DEEZER.find(string)?.value
            string.contains("tidal.com") -> REGEX_TIDAL.find(string)?.value
            string.contains("open.spotify.com") -> REGEX_SPOTIFY.find(string)?.value
            else -> {
                println("Could not find URI in string"); return null
            }
        }
        println("uriString: $uriString")
        return uriString
    }

    private val REGEX_SPOTIFY = "https://open\\.spotify\\.com/track/(\\w+)".toRegex()
    private val REGEX_DEEZER = "https://deezer\\.page\\.link/(\\w+)".toRegex()
    private val REGEX_TIDAL = "https://tidal\\.com/track/(\\d+)".toRegex()
}