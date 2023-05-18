package com.valerian.sharesong

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

class ShareSongApplication : Application() {
    val dataStoreSingleton by lazy { DataStoreSingleton(this) }
}

private val Context.dataStore by preferencesDataStore("user_preferences")

class DataStoreSingleton(private val context: Context) {
    fun getInstance() = context.dataStore
}