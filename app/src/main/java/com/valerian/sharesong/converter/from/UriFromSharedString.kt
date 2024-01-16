package com.valerian.sharesong.converter.from

import com.valerian.sharesong.ALLOWED_URLS

fun uriFromSharedString(string: String): String? {
    val uriString = ALLOWED_URLS.firstNotNullOfOrNull { it.find(string)?.value }

    if (uriString == null) {
        println("Could not find URI in string")
        return null
    }

    println("uriString: $uriString") // TODO remove from logs
    
    return uriString
}