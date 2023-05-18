package com.valerian.sharesong.send

import android.content.Intent
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
import com.valerian.sharesong.converter.from.UriFromSharedString
import com.valerian.sharesong.converter.from.convertFrom
import com.valerian.sharesong.converter.to.ToService
import com.valerian.sharesong.converter.to.convertTo
import com.valerian.sharesong.ui.composable.Greeting
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class ToActivity : ComponentActivity() {
    private var textShowingIntent: String? by mutableStateOf("Send Song Activity no shared intent")
    private var lastIntent: Intent? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            com.valerian.sharesong.ui.theme.ShareSongTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface) {
                    Greeting(textShowingIntent)
                }
            }
        }
    }

    abstract val targetService: ToService

    override fun onResume() {
        super.onResume()
        if (intent == lastIntent) return
        lastIntent = intent
        val intentString = intent.getStringExtra(Intent.EXTRA_TEXT) ?: return
        val intentUri = UriFromSharedString.get(intentString) ?: return
        if (allowedHosts.none { intentUri.host == it }) {
            Toast.makeText(this,
                "Sorry, this link is not supported",
                Toast.LENGTH_SHORT).show()
            return
        }
        textShowingIntent = intentUri.toString()

        CoroutineScope(Dispatchers.IO).launch {
            val track = convertFrom(intentUri) ?: return@launch

            val targetServiceUrl =
                convertTo(track, targetService) ?: return@launch

            val sendIntent = Intent.createChooser(Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, targetServiceUrl)
                type = "text/plain"
            }, null)

            startActivity(sendIntent)
        }
    }

    companion object {
        private val allowedHosts =
            listOf("open.spotify.com", "deezer.page.link")
    }
}