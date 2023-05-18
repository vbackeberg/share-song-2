package com.valerian.sharesong.send

import com.valerian.sharesong.converter.to.Spotify

class ToSpotifyActivity : ToActivity() {
    override val targetService = Spotify()
}