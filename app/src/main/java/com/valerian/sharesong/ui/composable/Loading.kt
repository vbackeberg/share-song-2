package com.valerian.sharesong.ui.composable

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.StartOffsetType
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class Loading {}

@Composable
fun Dot(modifier: Modifier = Modifier) {
    val colorScheme = MaterialTheme.colorScheme.secondary
    Canvas(modifier = modifier) {
        drawCircle(color = colorScheme, radius = size.minDimension / 2)
    }
}

@Composable
fun OscillatingDot(modifier: Modifier = Modifier, startOffset: Int
) {
    val offsetY = remember { Animatable(0f) }

    LaunchedEffect(key1 = true) {
        offsetY.animateTo(20f,
            animationSpec = infiniteRepeatable(animation = tween(500,
                easing = EaseInOut),
                repeatMode = RepeatMode.Reverse,
                initialStartOffset = StartOffset(startOffset,
                    StartOffsetType.Delay)))
    }

    Dot(modifier.offset(y = offsetY.value.dp))
}

@Composable
fun LoadingAnimation() {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 48.dp),
        horizontalArrangement = Arrangement.SpaceEvenly) {
        OscillatingDot(modifier = Modifier.size(20.dp), startOffset = 100)
        OscillatingDot(modifier = Modifier.size(20.dp), startOffset = 200)
        OscillatingDot(modifier = Modifier.size(20.dp), startOffset = 300)

    }
}
