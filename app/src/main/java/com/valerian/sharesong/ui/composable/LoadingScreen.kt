package com.valerian.sharesong.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.valerian.sharesong.ui.theme.ShareSongTheme


@Composable
fun LoadingScreen(name: String?) {
    Column(modifier = Modifier
        .padding(start = 16.dp, end = 16.dp, top = 64.dp, bottom = 16.dp)
        .fillMaxSize()) {

        Text(text = "Looking for your song",
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp))

        Spacer(modifier = Modifier.weight(1f))
        LoadingAnimation()
        Spacer(modifier = Modifier.weight(3f))


        Text(text = "Shared link $name!",
            fontSize = 8.sp,
            lineHeight = 10.sp,
            color = MaterialTheme.colorScheme.outline)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ShareSongTheme {
        LoadingScreen("MainActivity")
    }
}