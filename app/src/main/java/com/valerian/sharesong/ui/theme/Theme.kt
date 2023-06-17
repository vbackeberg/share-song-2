package com.valerian.sharesong.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.compose.md_theme_dark_background
import com.example.compose.md_theme_dark_onBackground
import com.example.compose.md_theme_dark_onPrimary
import com.example.compose.md_theme_dark_onSecondary
import com.example.compose.md_theme_dark_onSurface
import com.example.compose.md_theme_dark_onTertiary
import com.example.compose.md_theme_dark_primary
import com.example.compose.md_theme_dark_secondary
import com.example.compose.md_theme_dark_surface
import com.example.compose.md_theme_dark_tertiary
import com.example.compose.md_theme_light_background
import com.example.compose.md_theme_light_onBackground
import com.example.compose.md_theme_light_onPrimary
import com.example.compose.md_theme_light_onSecondary
import com.example.compose.md_theme_light_onSurface
import com.example.compose.md_theme_light_onTertiary
import com.example.compose.md_theme_light_primary
import com.example.compose.md_theme_light_secondary
import com.example.compose.md_theme_light_surface
import com.example.compose.md_theme_light_tertiary

private val DarkColorScheme = darkColorScheme(
    primary = md_theme_dark_primary,
    secondary = md_theme_dark_secondary,
    tertiary = md_theme_dark_tertiary,
    background = md_theme_dark_background,
    surface = md_theme_dark_surface,
    onPrimary = md_theme_dark_onPrimary,
    onSecondary = md_theme_dark_onSecondary,
    onTertiary = md_theme_dark_onTertiary,
    onBackground = md_theme_dark_onBackground,
    onSurface = md_theme_dark_onSurface,
)

private val LightColorScheme = lightColorScheme(
    primary = md_theme_light_primary,
    secondary = md_theme_light_secondary,
    tertiary = md_theme_light_tertiary,
    background = md_theme_light_background,
    surface = md_theme_light_surface,
    onPrimary = md_theme_light_onPrimary,
    onSecondary = md_theme_light_onSecondary,
    onTertiary = md_theme_light_onTertiary,
    onBackground = md_theme_light_onBackground,
    onSurface = md_theme_light_onSurface,
)

@Composable
fun ShareSongTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(
                context
            )
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.apply {
                statusBarColor = colorScheme.background.toArgb()
                navigationBarColor = colorScheme.background.toArgb()
            }
            WindowCompat.getInsetsController(
                window,
                view
            ).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}