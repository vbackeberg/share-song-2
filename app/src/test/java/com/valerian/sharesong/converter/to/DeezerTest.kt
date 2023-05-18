package com.valerian.sharesong.converter.to

import com.valerian.sharesong.converter.Track
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class DeezerTest {

    @Test
    fun run() = runTest {
        val url = Deezer().getTrackUrl(Track("Mockingbird", "eminem"))

        assertEquals("https://www.deezer.com/track/1109739", url)

    }
}