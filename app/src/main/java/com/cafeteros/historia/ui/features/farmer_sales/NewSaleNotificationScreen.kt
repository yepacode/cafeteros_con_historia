package com.cafeteros.historia.ui.features.farmer_sales

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity contenedora de [NewSaleNotificationScreen].
 *
 * Estilo "modal" — visualmente es una tarjeta centrada con padding generoso.
 * El usuario sale con la X o tocando los CTAs (que abren otras pantallas).
 *
 * En producción esta pantalla la disparará el sistema de push cuando llegue
 * una venta nueva. Hoy se accede manualmente desde el icono de campana del
 * panel para demo.
 */
class NewSaleNotificationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                NewSaleNotificationScreen(
                    onDismiss = ::finish,
                    onSeeOrder = {
                        OrderDetailActivity.start(this, "OR-34521")
                        finish()
                    },
                    onSeeAllOrders = {
                        SalesActivity.start(this)
                        finish()
                    }
                )
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, NewSaleNotificationActivity::class.java))
        }
    }
}

/**
 * Notificación de "Nueva venta" — tarjeta de celebración con resumen del
 * pedido recién recibido y dos CTAs: ver pedido / ver todos.
 *
 * Datos mock por ahora; en producción vendrán del payload de la
 * notificación push.
 */
@Composable
fun NewSaleNotificationScreen(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onSeeOrder: () -> Unit,
    onSeeAllOrders: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = BrandSpacing.lg, vertical = BrandSpacing.md),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Cerrar (X) arriba a la derecha
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            IconButton(onClick = onDismiss) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Cerrar",
                    tint = BrandColors.TextSecondary
                )
            }
        }

        Text(text = "🎉", fontSize = 48.sp)
        Spacer(modifier = Modifier.height(BrandSpacing.sm))
        Text(
            text = "¡Nueva venta!",
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = BrandColors.TextPrimary
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "María G. acaba de comprar tu café",
            color = BrandColors.TextSecondary,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(BrandSpacing.lg))

        // Tarjeta resumen del pedido
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(BrandColors.CardBackground, RoundedCornerShape(16.dp))
                .padding(BrandSpacing.md),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "PEDIDO #OR-34521",
                color = BrandColors.TextSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
            )
            ItemLine(name = "Café Huila 250g", quantity = 2)
            ItemLine(name = "Café Nariño 500g", quantity = 1)
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(BrandColors.DividerLine))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Venta total", color = BrandColors.TextSecondary, fontSize = 13.sp)
                Text(
                    text = "\$146.000",
                    color = Color(0xFFC9A24A),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE8F4EC), RoundedCornerShape(8.dp))
                    .padding(horizontal = BrandSpacing.md, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Recibirás:", color = BrandColors.TextPrimary, fontSize = 13.sp)
                    Text(
                        text = "\$126.720",
                        color = BrandColors.FarmerPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(BrandSpacing.sm))
        Text(
            text = "🟡 EMPACA ANTES DE MAÑANA 9 AM",
            color = Color(0xFF8C6E1F),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )

        Spacer(modifier = Modifier.height(BrandSpacing.lg))

        Button(
            onClick = onSeeOrder,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BrandColors.FarmerPrimary,
                contentColor = Color.White
            )
        ) {
            Text(text = "Ver pedido completo", fontWeight = FontWeight.SemiBold)
        }
        TextButton(onClick = onSeeAllOrders, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Ver todos mis pedidos",
                color = BrandColors.TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(modifier = Modifier.height(BrandSpacing.sm))
        Text(
            text = "💚 Gracias por trabajar con Origen",
            color = BrandColors.TextSecondary,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun ItemLine(name: String, quantity: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(BrandColors.InputBackground, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) { Text(text = "📷", fontSize = 16.sp) }
        Spacer(modifier = Modifier.size(BrandSpacing.sm))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = name, color = BrandColors.TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Text(text = "Cantidad: $quantity", color = BrandColors.TextSecondary, fontSize = 11.sp)
        }
    }
}
