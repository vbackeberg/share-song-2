package com.valerian.sharesong

val REGEX_SPOTIFY = "https://open\\.spotify\\.com/track/\\w+".toRegex()
val REGEX_DEEZER = "https://deezer\\.page\\.link/\\w+".toRegex()
val REGEX_TIDAL = "https://tidal\\.com/track/\\d+".toRegex()
val REGEX_APPLE_MUSIC = "https://music\\.apple\\.com/[A-Z]{2}/album/\\w+/\\d+\\?i=\\d+".toRegex()

val ALLOWED_URLS = listOf(
    REGEX_SPOTIFY,
    REGEX_DEEZER,
    REGEX_TIDAL,
    REGEX_APPLE_MUSIC
)
