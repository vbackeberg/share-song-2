package com.valerian.sharesong

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.valerian.sharesong.ui.theme.ShareSongTheme
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SettingsActivity : ComponentActivity() {
    private val items = mutableMapOf(
        ("Spotify" to "Spotify"),
        ("Deezer" to "Deezer"),
        ("Tidal" to "Tidal"),
        ("AppleMusic" to "Apple Music"),
        ("YoutubeMusic" to "Youtube Music")
    )

    @OptIn(ExperimentalLayoutApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dataStore = (application as ShareSongApplication).dataStoreSingleton.getInstance()
        val targetService = dataStore.data.map { it[SERVICE_OF_USER].orEmpty() }

        setContent {
            ShareSongTheme {
                Surface(color = MaterialTheme.colorScheme.surface) {
                    val scrollState = ScrollState(0)

                    Column(
                        modifier = Modifier
                            .padding(16.dp, 48.dp)
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                    ) {

                        val selectedService = targetService.collectAsStateWithLifecycle(
                            initialValue = "no"
                        )
                        val coroutineScope = rememberCoroutineScope()

                        Text(
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            fontSize = 24.sp,
                            text = "Select your music service",
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                        )

                        Text(
                            fontSize = 16.sp,
                            text = "Choose the music service we should convert your song to. Next, select and share any song link and look for the Share Song icon. Tap it to convert your song!",
                            modifier = Modifier.padding(16.dp)
                        )

                        FlowRow(
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            val itemKeys = items.keys.toList()
                            itemKeys.forEachIndexed { index, _ ->
                                ButtonWithOutline (
                                    text = items[itemKeys[index]].orEmpty(),
                                    isSelected = itemKeys[index] == selectedService.value,
                                    onSelected = {
                                        coroutineScope.launch {
                                            dataStore.edit { preferences ->
                                                preferences[SERVICE_OF_USER] = itemKeys[index]
                                            }
                                            setTitle(R.string.title_activity_to_deezer)
                                        }
                                    })
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        val SERVICE_OF_USER = stringPreferencesKey("serviceOfUser")
    }
}

@Composable
fun ButtonWithOutline(
    text: String, isSelected: Boolean, onSelected: () -> Unit
) {
    Button(
        colors = if (isSelected) ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ) else ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        shape = RoundedCornerShape(16.dp),
        onClick = onSelected,
        modifier = Modifier
            .padding(16.dp)
            .height(96.dp)
            .width(128.dp),
        contentPadding = PaddingValues(8.dp),
    ) {
        Text(text = text, fontSize = 20.sp, textAlign = TextAlign.Center)
    }
}
