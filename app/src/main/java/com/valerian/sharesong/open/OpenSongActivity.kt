package com.valerian.sharesong.open

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.datastore.preferences.core.stringPreferencesKey
import com.valerian.sharesong.ShareSongApplication
import com.valerian.sharesong.converter.from.convertFrom
import com.valerian.sharesong.converter.to.convertTo
import com.valerian.sharesong.ui.composable.Greeting
import com.valerian.sharesong.ui.theme.ShareSongTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import com.valerian.sharesong.converter.to.Deezer as ToDeezer
import com.valerian.sharesong.converter.to.Spotify as ToSpotify

class OpenSongActivity : ComponentActivity() {
    private var textShowingIntent: String? by mutableStateOf("no shared intent")
    private var lastIntent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ShareSongTheme {
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface) {
                    Greeting(textShowingIntent)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (intent == lastIntent) return
        lastIntent = intent
        val intentUri = intent.data ?: return
        textShowingIntent = intentUri.toString()
        val dataStore =
            (application as ShareSongApplication).dataStoreSingleton.getInstance()

        CoroutineScope(Dispatchers.IO).launch {
            val track = convertFrom(intentUri) ?: return@launch

            val targetServiceName =
                dataStore.data.firstOrNull()
                    ?.get(stringPreferencesKey("serviceOfUser"))

            val targetService = when (targetServiceName) {
                "Spotify" -> ToSpotify()
                "Deezer" -> ToDeezer()
                else -> {
                    print("stored targetService $targetServiceName does not match any host")
                    return@launch
                }
            }

            val targetServiceUrl =
                convertTo(track, targetService) ?: return@launch

            startActivity(Intent(Intent.ACTION_VIEW,
                Uri.parse(targetServiceUrl)))
        }
    }
}