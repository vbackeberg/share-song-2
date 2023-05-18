package com.valerian.sharesong.send

import com.valerian.sharesong.converter.to.Deezer

class ToDeezerActivity : ToActivity() {
    override val targetService = Deezer()
}