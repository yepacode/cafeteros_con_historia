package com.cafeteros.historia.ui.features.cart

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cafeteros.historia.ui.features.checkoutshipping.ShippingAddressActivity
import com.cafeteros.historia.ui.theme.CafeterosTheme

class CartActivity : ComponentActivity() {

    private val viewModel: CartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                val state by viewModel.uiState.collectAsStateWithLifecycle()
                CartScreen(
                    state = state,
                    onBack = ::finish,
                    onInc = viewModel::incQuantity,
                    onDec = viewModel::decQuantity,
                    onRemove = viewModel::remove,
                    onCheckout = { ShippingAddressActivity.start(this) }
                )
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, CartActivity::class.java))
        }
    }
}
