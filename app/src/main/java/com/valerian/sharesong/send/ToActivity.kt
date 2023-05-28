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
import com.valerian.sharesong.ShareSongClient
import com.valerian.sharesong.converter.from.UriFromSharedString
import com.valerian.sharesong.ui.composable.Greeting
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.await

abstract class ToActivity : ComponentActivity() {
    private var textShowingIntent: String? by mutableStateOf("Send Song Activity no shared intent")
    private var lastIntent: Intent? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            com.valerian.sharesong.ui.theme.ShareSongTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Greeting(textShowingIntent)
                }
            }
        }
    }

    abstract val targetService: String

    override fun onResume() {
        super.onResume()
        if (intent == lastIntent) return
        lastIntent = intent
        val intentString = intent.getStringExtra(Intent.EXTRA_TEXT)
        if (intentString.isNullOrBlank()) {
            println("intentString is null")
            toastNotSupported()
            return
        }

        val intentUri = UriFromSharedString.get(intentString)
        if (intentUri.isNullOrBlank()) {
            toastNotSupported()
            return
        }

        textShowingIntent = intentUri

        CoroutineScope(Dispatchers.IO).launch {
            val targetServiceUrl =
                ShareSongClient.instance.convert(intentUri, targetService).await().string()

            val sendIntent = Intent.createChooser(Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, targetServiceUrl)
                type = "text/plain"
            }, null)

            startActivity(sendIntent)
        }
    }

    private fun toastNotSupported() {
        Toast.makeText(
            this,
            "Sorry, this link is not supported",
            Toast.LENGTH_SHORT
        ).show()
    }
}