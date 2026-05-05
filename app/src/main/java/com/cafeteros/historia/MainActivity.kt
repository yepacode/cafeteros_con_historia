package com.cafeteros.historia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cafeteros.historia.ui.features.home.HomeScreen
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity de la home, destino final del flujo Splash → Onboarding → Home.
 *
 * Es Compose puro (no usa layouts XML) y delega toda la UI a [HomeScreen].
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                HomeScreen()
            }
        }
    }
}
