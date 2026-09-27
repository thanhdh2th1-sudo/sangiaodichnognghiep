package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = AgriGreenDark,
    onPrimary = Color(0xFF003913),
    primaryContainer = Color(0xFF00531D),
    onPrimaryContainer = Color(0xFF9DF49E),
    secondary = AgriBlueDark,
    onSecondary = Color(0xFF00344F),
    secondaryContainer = Color(0xFF004C70),
    onSecondaryContainer = Color(0xFFC7E7FF),
    tertiary = AgriAmberDark,
    onTertiary = Color(0xFF432C00),
    background = AgriBackgroundDark,
    onBackground = AgriTextPrimaryDark,
    surface = AgriSurfaceDark,
    onSurface = AgriTextPrimaryDark,
    surfaceVariant = AgriSurfaceVariantDark,
    onSurfaceVariant = AgriTextSecondaryDark,
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005)
)

private val LightColorScheme = lightColorScheme(
    primary = AgriGreenPrimary,
    onPrimary = Color.White,
    primaryContainer = AgriGreenLight,
    onPrimaryContainer = Color(0xFF002107),
    secondary = AgriGreenSecondary,
    onSecondary = Color.White,
    secondaryContainer = AgriBlueLight,
    onSecondaryContainer = Color(0xFF001F2A),
    tertiary = AgriAmberAccent,
    onTertiary = Color.White,
    tertiaryContainer = AgriAmberLight,
    onTertiaryContainer = Color(0xFF281800),
    background = AgriBackground,
    onBackground = AgriTextPrimary,
    surface = AgriSurface,
    onSurface = AgriTextPrimary,
    surfaceVariant = AgriSurfaceVariant,
    onSurfaceVariant = AgriTextSecondary,
    error = AgriError,
    onError = Color.White
)

@Composable
fun AgriMarketplaceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
