package com.valerian.sharesong.send

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.stringPreferencesKey
import com.valerian.sharesong.ShareSongApplication
import com.valerian.sharesong.ShareSongClient
import com.valerian.sharesong.converter.from.uriFromSharedString
import com.valerian.sharesong.ui.composable.LoadingScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.await

class SendActivity : ComponentActivity() {
    private var textShowingIntent: String? by mutableStateOf("Send Song Activity no shared intent")
    private var lastIntent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            com.valerian.sharesong.ui.theme.ShareSongTheme {
                LoadingScreen(
                    textShowingIntent
                )
            }
        }
    }

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

        val dataStore = (application as ShareSongApplication).dataStoreSingleton.getInstance()

        CoroutineScope(Dispatchers.IO).launch {
            val targetServiceName =
                dataStore.data.firstOrNull()?.get(stringPreferencesKey("serviceOfUser"))

            if (targetServiceName == null) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@SendActivity,
                        "Open Share Song settings, and choose the service you want to convert to.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return@launch
            }

            val response = try {
                ShareSongClient.instance.convert(intentUri, targetServiceName).await()
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@SendActivity,
                        "Sorry, your song could not be converted. Maybe, it wasn't found on ${targetServiceName}.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return@launch
            }

            startActivity(
                Intent(
                    Intent.ACTION_VIEW, Uri.parse(response.targetServiceUrl)
                )
            )
        }
    }

    private fun toastNotSupported() {
        Toast.makeText(
            this, "Sorry, this link is not supported. You can only share songs.", Toast.LENGTH_SHORT
        ).show()
        finish()
    }

}
