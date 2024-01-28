package com.valerian.sharesong.send

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.valerian.sharesong.ShareSongClient
import com.valerian.sharesong.converter.from.uriFromSharedString
import com.valerian.sharesong.ui.composable.LoadingScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.await

abstract class ToActivity(private val targetService: String) : ComponentActivity() {
    private var textShowingIntent: String? by mutableStateOf("Send Song Activity no shared intent")
    private var lastIntent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            com.valerian.sharesong.ui.theme.ShareSongTheme {

                LoadingScreen(
                    onDismissRequest = {},
                    textShowingIntent
                )

            }
        }
    }

    open val targetServiceDisplayName = targetService

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

        val intentUri = uriFromSharedString(intentString)
        if (intentUri.isNullOrBlank()) {
            toastNotSupported()
            return
        }

        textShowingIntent = intentUri

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ShareSongClient.instance.convert(intentUri, targetService).await()

                val sendIntent = Intent.createChooser(Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "Shared from ${response.originService} to $targetServiceDisplayName because I can. \uD83D\uDE0E" +
                                "${System.lineSeparator()}${response.targetServiceUrl}"
                    )
                    type = "text/plain"
                }, null)

                startActivity(sendIntent)

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@ToActivity,
                        "Sorry, your song could not be converted. Maybe, it wasn't found on ${targetServiceDisplayName}.",
                        Toast.LENGTH_SHORT
                    ).show()
                    2
                }
            }

            finish()
        }
    }

    private fun toastNotSupported() {
        Toast.makeText(
            this, "Sorry, this link is not supported", Toast.LENGTH_SHORT
        ).show()
        finish()
    }

}
