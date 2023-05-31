package com.valerian.sharesong.open

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
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
import com.valerian.sharesong.ShareSongClient
import com.valerian.sharesong.ui.composable.Greeting
import com.valerian.sharesong.ui.theme.ShareSongTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import retrofit2.await

class OpenSongActivity : ComponentActivity() {
    private var textShowingIntent: String? by mutableStateOf("no shared intent")
    private var lastIntent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ShareSongTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface
                ) {
                    Greeting(textShowingIntent)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (intent == lastIntent) return
        lastIntent = intent
        val intentUri = intent.data?.toString() ?: return
        textShowingIntent = intentUri

        if (allowedUrls.none { it.containsMatchIn(intentUri) }) {
            Toast.makeText(
                this, "Sorry, this link is not supported", Toast.LENGTH_SHORT
            ).show()
            return
        }

        val dataStore = (application as ShareSongApplication).dataStoreSingleton.getInstance()

        CoroutineScope(Dispatchers.IO).launch {
            val targetServiceName =
                dataStore.data.firstOrNull()?.get(stringPreferencesKey("serviceOfUser"))
                    ?: return@launch


            val targetServiceUrl =
                ShareSongClient.instance.convert(intentUri, targetServiceName).await().string()

            startActivity(
                Intent(
                    Intent.ACTION_VIEW, Uri.parse(targetServiceUrl)
                )
            )
        }
    }

    companion object {
        private val allowedUrls = listOf(
            "https://open\\.spotify\\.com/track/(\\w+)".toRegex(),
            "https://deezer\\.page\\.link/(\\w+)".toRegex()
        )
    }
}