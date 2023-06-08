package com.valerian.sharesong

import junit.framework.TestCase.assertTrue
import org.junit.Test

class OriginUrlsTest {
    @Test
    fun regexAppleMusic() {
        assertTrue(REGEX_APPLE_MUSIC.containsMatchIn("https://music.apple.com/us/album/love-tonight-david-guetta-remix-edit/1570383648?i=1570383649"))
    }

}