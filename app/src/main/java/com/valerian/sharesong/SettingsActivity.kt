package com.valerian.sharesong

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
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
    private val items =
        mutableStateListOf("Spotify", "Deezer", "Tidal")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dataStore =
            (application as ShareSongApplication).dataStoreSingleton.getInstance()
        val targetServiceFlow =
            dataStore.data.map { it[SERVICE_OF_USER].orEmpty() }

        setContent {
            ShareSongTheme {
                Surface(color = MaterialTheme.colorScheme.surface) {
                    Column(modifier = Modifier
                        .padding(16.dp, 48.dp)
                        .fillMaxSize()) {

                        val selectedService =
                            targetServiceFlow.collectAsStateWithLifecycle(
                                initialValue = "no")
                        val coroutineScope = rememberCoroutineScope()

                        Text(fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            fontSize = 24.sp,
                            text = "Choose your music service",
                            modifier = Modifier.padding(16.dp))

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            itemsIndexed(items) { index, _ ->
                                ButtonWithOutline(text = items[index],
                                    isSelected = items[index] == selectedService.value,
                                    onSelected = {
                                        coroutineScope.launch {
                                            dataStore.edit { preferences ->
                                                preferences[SERVICE_OF_USER] =
                                                    items[index]
                                            }
                                        }
                                    })
                            }
                        }

                        Text(
                            fontSize = 16.sp,
                            text = "If you click a music link, it will be opened in your own music app.",
                            modifier = Modifier.padding(16.dp))
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
fun ButtonWithOutline(text: String, isSelected: Boolean, onSelected: () -> Unit
) {
    Button(
        colors = if (isSelected) ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ) else ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer),
        shape = RoundedCornerShape(16.dp),
        onClick = onSelected,
        modifier = Modifier
            .padding(16.dp)
            .height(128.dp),
        contentPadding = PaddingValues(8.dp),
    ) {
        Text(text = text, fontSize = 20.sp)
    }
}
