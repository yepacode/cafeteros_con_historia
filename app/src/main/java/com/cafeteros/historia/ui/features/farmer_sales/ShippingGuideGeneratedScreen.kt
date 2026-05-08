package com.cafeteros.historia.ui.features.farmer_sales

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Print
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.cafeteros.historia.MainActivity
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme

class ShippingGuideGeneratedActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val orderNumber = intent.getStringExtra(EXTRA_ORDER_NUMBER) ?: "—"

        setContent {
            CafeterosTheme {
                ShippingGuideGeneratedScreen(
                    orderNumber = orderNumber,
                    onBack = ::finish,
                    onPrintLabel = ::soonToast,
                    onDownloadPdf = ::soonToast,
                    onSendByWhatsApp = ::soonToast,
                    onMarkDispatched = ::backToPanel
                )
            }
        }
    }

    private fun soonToast(action: String = "Acción") {
        Toast.makeText(this, "Próximamente: $action", Toast.LENGTH_SHORT).show()
    }

    private fun backToPanel() {
        Toast.makeText(this, "Pedido marcado como despachado", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }

    companion object {
        private const val EXTRA_ORDER_NUMBER = "extra_order_number"
        private const val EXTRA_CARRIER_ID = "extra_carrier_id"

        fun start(context: Context, orderNumber: String, carrierId: String) {
            val intent = Intent(context, ShippingGuideGeneratedActivity::class.java)
                .putExtra(EXTRA_ORDER_NUMBER, orderNumber)
                .putExtra(EXTRA_CARRIER_ID, carrierId)
            context.startActivity(intent)
        }
    }
}

/**
 * Pantalla "Guía Generada" — confirmación de que la guía está lista.
 * Muestra los datos del envío en formato de etiqueta y los botones de
 * acciones (imprimir, descargar PDF, WhatsApp, marcar despachado).
 */
@Composable
fun ShippingGuideGeneratedScreen(
    modifier: Modifier = Modifier,
    orderNumber: String,
    onBack: () -> Unit,
    onPrintLabel: (String) -> Unit,
    onDownloadPdf: (String) -> Unit,
    onSendByWhatsApp: (String) -> Unit,
    onMarkDispatched: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding()
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = BrandSpacing.sm, vertical = BrandSpacing.xs),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Volver",
                    tint = BrandColors.TextPrimary
                )
            }
            Text(
                text = "Guía Generada",
                modifier = Modifier.weight(1f),
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BrandColors.TextPrimary
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.size(48.dp))
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = BrandSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
        ) {
            Spacer(modifier = Modifier.height(BrandSpacing.sm))

            // Confirmación visual
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(BrandColors.FarmerPrimary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }
                Spacer(modifier = Modifier.height(BrandSpacing.sm))
                Text(
                    text = "¡Listo para despachar!",
                    style = TextStyle(
                        fontFamily = FontFamily.Serif,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandColors.TextPrimary
                    )
                )
                Text(
                    text = "Tu café está un paso más cerca de su destino.",
                    color = BrandColors.TextSecondary,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            }

            // Card de la guía
            ShippingLabelCard(orderNumber = orderNumber)

            // Acciones del envío
            Text(
                text = "Acciones del envío",
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BrandColors.TextPrimary
                )
            )
            Button(
                onClick = { onPrintLabel("Imprimir etiqueta") },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandColors.CoffeeBrown,
                    contentColor = Color.White
                )
            ) {
                Icon(Icons.Outlined.Print, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.size(8.dp))
                Text(text = "Imprimir etiqueta", fontWeight = FontWeight.SemiBold)
            }
            OutlinedButton(
                onClick = { onDownloadPdf("Descargar PDF") },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Outlined.Download, contentDescription = null, modifier = Modifier.size(18.dp), tint = BrandColors.TextPrimary)
                Spacer(modifier = Modifier.size(8.dp))
                Text(text = "Descargar PDF", color = BrandColors.TextPrimary, fontWeight = FontWeight.SemiBold)
            }
            OutlinedButton(
                onClick = { onSendByWhatsApp("Enviar por WhatsApp") },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Outlined.Share, contentDescription = null, modifier = Modifier.size(18.dp), tint = BrandColors.TextPrimary)
                Spacer(modifier = Modifier.size(8.dp))
                Text(text = "Enviar por WhatsApp", color = BrandColors.TextPrimary, fontWeight = FontWeight.SemiBold)
            }

            // Aviso amarillo
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFF1B8), RoundedCornerShape(12.dp))
                    .padding(BrandSpacing.md),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.WarningAmber,
                    contentDescription = null,
                    tint = Color(0xFF8C6E1F),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "Avisa al comprador cuando entregues el paquete al transportador. " +
                            "Marca el pedido como despachado para que se actualice su estado.",
                    color = BrandColors.TextPrimary,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(BrandSpacing.sm))
            Button(
                onClick = onMarkDispatched,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandColors.FarmerPrimary,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Marcar como Despachado 📦", fontWeight = FontWeight.SemiBold)
            }
            Spacer(modifier = Modifier.height(BrandSpacing.lg))
        }
    }
}

@Composable
private fun ShippingLabelCard(orderNumber: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .padding(BrandSpacing.md),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Text(
            text = "Guía Generada",
            color = BrandColors.TextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.5.sp
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "TRANSPORTADORA", color = BrandColors.TextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = "El Caficultor",
                    color = BrandColors.TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )
                Text(text = "LOGÍSTICA", color = BrandColors.TextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }
            Column(horizontalAlignment = Alignment.End) {
                Box(
                    modifier = Modifier
                        .background(BrandColors.CoffeeBrown, RoundedCornerShape(50))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "EXPRESS",
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = orderNumber.replace("OR-", ""),
                    color = BrandColors.TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(BrandColors.DividerLine))

        // Remitente
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(text = "REMITENTE / EMISOR", color = BrandColors.TextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
            Text(text = "Don Alberto", color = BrandColors.TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Text(text = "Finca La Esperanza", color = BrandColors.TextSecondary, fontSize = 12.sp)
            Text(text = "Pitalito, Huila, Colombia", color = BrandColors.TextSecondary, fontSize = 12.sp)
        }

        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(BrandColors.DividerLine))

        // Destinatario
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(text = "DESTINATARIO / RECEPTOR", color = BrandColors.TextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
            Text(text = "María G.", color = BrandColors.TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Text(text = "Carrera 7 # 71-21, Apto 902", color = BrandColors.TextSecondary, fontSize = 12.sp)
            Text(text = "Bogotá, D.C., Colombia", color = BrandColors.TextSecondary, fontSize = 12.sp)
        }
    }
}
