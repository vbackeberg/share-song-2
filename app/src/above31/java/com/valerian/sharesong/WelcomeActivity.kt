package com.valerian.sharesong

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.valerian.sharesong.ui.theme.ShareSongTheme

class WelcomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current
            ShareSongTheme {
                Surface(color = MaterialTheme.colorScheme.surface) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp, 48.dp)
                            .fillMaxSize()
                    ) {
                        Text(
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            fontSize = 24.sp,
                            text = "Sharing songs with others made easy",
                            modifier = Modifier.padding(16.dp)
                        )

                        Text(
                            fontSize = 16.sp,
                            text = "This app can convert links you share from music services to other services, e.g. from Spotify to Deezer. " +
                                    "Look out for the Share Song icons in your sharing options.",
                            modifier = Modifier.padding(16.dp)
                        )

                        Button(
                            onClick = {
                                (context as? Activity)?.finish()
                            },
                            modifier = Modifier
                                .padding(vertical = 64.dp)
                                .align(Alignment.CenterHorizontally)
                                .height(64.dp)
                                .width(256.dp),
                            shape = MaterialTheme.shapes.extraLarge
                        ) {
                            Text(text = "Cool", fontSize = 32.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
