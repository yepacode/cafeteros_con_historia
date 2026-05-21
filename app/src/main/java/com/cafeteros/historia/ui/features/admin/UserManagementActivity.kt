package com.cafeteros.historia.ui.features.admin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity contenedora de [UserManagementScreen]. Cablea el [UserManagementViewModel]
 * con la pantalla y maneja la navegación de retorno.
 */
class UserManagementActivity : ComponentActivity() {

    private val viewModel: UserManagementViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                UserManagementScreen(
                    viewModel = viewModel,
                    onBack = ::finish
                )
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, UserManagementActivity::class.java))
        }
    }
}
