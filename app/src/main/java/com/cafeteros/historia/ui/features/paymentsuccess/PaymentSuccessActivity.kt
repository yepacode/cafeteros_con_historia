package com.cafeteros.historia.ui.features.paymentsuccess

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.features.buyer_catalog.BuyerCatalogActivity
import com.cafeteros.historia.ui.features.buyer_catalog.MyPurchasesActivity
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity de "pago exitoso" tras crear la(s) Order en Firestore.
 *
 * Recibe los datos básicos del pedido por intent extras (no necesita
 * volver a leer Firestore — ya tenemos lo importante). Da dos vías:
 * "Ver mis pedidos" o "Seguir comprando".
 */
class PaymentSuccessActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val orderId = intent.getStringExtra(EXTRA_ORDER_ID).orEmpty()
        val totalCop = intent.getLongExtra(EXTRA_TOTAL_COP, 0L)
        val address = intent.getStringExtra(EXTRA_ADDRESS).orEmpty()

        setContent {
            CafeterosTheme {
                PaymentSuccessScreen(
                    orderId = orderId,
                    totalCop = totalCop,
                    address = address,
                    onSeeOrders = {
                        MyPurchasesActivity.start(this)
                        backToCatalog()
                    },
                    onKeepShopping = ::backToCatalog
                )
            }
        }
    }

    /**
     * Cierra esta activity y vuelve al catálogo en lugar de quedarse en
     * la pila. Usa CLEAR_TASK para que el back del comprador no caiga en
     * el checkout ya consumido.
     */
    private fun backToCatalog() {
        val intent = Intent(this, BuyerCatalogActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }

    companion object {
        private const val EXTRA_ORDER_ID = "extra_order_id"
        private const val EXTRA_TOTAL_COP = "extra_total_cop"
        private const val EXTRA_ADDRESS = "extra_address"

        fun start(context: Context, orderId: String, totalCop: Long, address: String) {
            val intent = Intent(context, PaymentSuccessActivity::class.java).apply {
                putExtra(EXTRA_ORDER_ID, orderId)
                putExtra(EXTRA_TOTAL_COP, totalCop)
                putExtra(EXTRA_ADDRESS, address)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            context.startActivity(intent)
        }
    }
}

@Composable
private fun PaymentSuccessScreen(
    orderId: String,
    totalCop: Long,
    address: String,
    onSeeOrders: () -> Unit,
    onKeepShopping: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding()
            .padding(BrandSpacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Anillo + check
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(BrandColors.CardBackground, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .background(Color(0xFFC9A227), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(56.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(BrandSpacing.lg))

        Text(
            text = "¡Pago confirmado!",
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = BrandColors.TextPrimary
            ),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Tu pedido fue creado y el caficultor ya lo recibió.",
            color = BrandColors.TextSecondary,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            lineHeight = 19.sp
        )

        Spacer(modifier = Modifier.height(BrandSpacing.lg))

        // Card resumen
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
                .padding(BrandSpacing.md),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            ReceiptRow(label = "Pedido", value = "#${orderId.take(8).uppercase()}")
            ReceiptRow(
                label = "Total pagado",
                value = "$" + "%,d".format(totalCop).replace(',', '.'),
                emphasize = true
            )
            if (address.isNotBlank()) {
                ReceiptRow(label = "Enviar a", value = address)
            }
        }

        Spacer(modifier = Modifier.height(BrandSpacing.lg))

        Button(
            onClick = onSeeOrders,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BrandColors.CoffeeBrown,
                contentColor = Color.White
            )
        ) {
            Text(text = "Ver mis pedidos", fontWeight = FontWeight.SemiBold)
        }
        Spacer(modifier = Modifier.height(BrandSpacing.sm))
        OutlinedButton(
            onClick = onKeepShopping,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text(
                text = "Seguir comprando",
                color = BrandColors.FarmerPrimary,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun ReceiptRow(label: String, value: String, emphasize: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = BrandColors.TextSecondary,
            fontSize = 12.sp,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            color = BrandColors.TextPrimary,
            fontSize = if (emphasize) 16.sp else 13.sp,
            fontWeight = if (emphasize) FontWeight.Bold else FontWeight.SemiBold,
            fontFamily = if (emphasize) FontFamily.Serif else FontFamily.SansSerif
        )
    }
}
