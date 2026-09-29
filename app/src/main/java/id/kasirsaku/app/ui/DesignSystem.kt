package id.kasirsaku.app.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Forest = Color(0xFF315C45)
private val Canvas = Color(0xFFF5F5EF)
private val Sage = Color(0xFFE5EEE7)
private val Ink = Color(0xFF20251F)

@Composable
fun KasirSakuTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Forest,
            onPrimary = Color.White,
            primaryContainer = Sage,
            onPrimaryContainer = Forest,
            background = Canvas,
            surface = Color.White,
            onSurface = Ink,
            secondary = Color(0xFF667B68),
            error = Color(0xFFA34D3C),
        ),
        content = content,
    )
}
