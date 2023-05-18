package com.valerian.sharesong

class Secrets {

    // Method calls will be added by gradle task hideSecret
    // Example : external fun getWellHiddenSecret(packageName: String): String

    companion object {
        init {
            System.loadLibrary("secrets")
        }
    }

    external fun getclientId4(packageName: String): String

    external fun getclientSecret4(packageName: String): String
}