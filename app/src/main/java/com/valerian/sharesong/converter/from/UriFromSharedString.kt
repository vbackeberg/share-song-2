package com.valerian.sharesong.converter.from

import android.net.Uri


object UriFromSharedString {
    fun get(string: String): Uri? {
        val uriString = when {
            string.contains("deezer.page.link") -> REGEX_DEEZER.find(string)?.value
            listOf("open.spotify.com").any {
                string.contains(it)
            } -> string

            else -> null
        }
        if (uriString == null) {
            println("Could not find URI in string")
            return null
        }
        println(uriString)
        return Uri.parse(uriString)
    }

    private val REGEX_DEEZER = "https://deezer\\.page\\.link/(\\w+)".toRegex()
}