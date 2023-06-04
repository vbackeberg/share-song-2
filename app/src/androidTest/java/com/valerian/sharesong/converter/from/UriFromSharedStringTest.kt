package com.valerian.sharesong.converter.from

import android.net.Uri
import junit.framework.TestCase.assertEquals
import org.junit.Test

class UriFromSharedStringTest {

    @Test
    fun deezer() {
        val sharedString =
            "I've found a song for you... Auf & Ab (Piano Version) by Montez \uD83D\uDD25 Listen now on #Deezer https://deezer.page.link/PpY5eGkh9tJcD62o6"

        assertEquals(
            Uri.parse("https://deezer.page.link/PpY5eGkh9tJcD62o6"),
            uriFromSharedString(sharedString)
        )
    }
}