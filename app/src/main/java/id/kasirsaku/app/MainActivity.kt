package id.kasirsaku.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import id.kasirsaku.app.ui.KasirSakuApp
import id.kasirsaku.app.ui.KasirSakuTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { KasirSakuTheme { KasirSakuApp() } }
    }
}
