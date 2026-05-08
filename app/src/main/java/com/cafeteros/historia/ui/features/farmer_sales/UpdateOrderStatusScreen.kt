package com.cafeteros.historia.ui.features.farmer_sales

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.features.farmer_registration.components.LabeledDropdown
import com.cafeteros.historia.ui.features.farmer_registration.components.LabeledTextField
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity contenedora de [UpdateOrderStatusScreen]. Recibe el `orderNumber`
 * por intent. Al confirmar, regresa a la pantalla anterior con un Toast
 * de éxito (en producción haría POST al backend para mover el estado).
 */
class UpdateOrderStatusActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val orderNumber = intent.getStringExtra(EXTRA_ORDER_NUMBER) ?: "—"

        setContent {
            CafeterosTheme {
                UpdateOrderStatusScreen(
                    orderNumber = orderNumber,
                    onDismiss = ::finish,
                    onConfirm = { newStatus ->
                        Toast.makeText(
                            this,
                            "Pedido $orderNumber → $newStatus (próximamente con backend)",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                )
            }
        }
    }

    companion object {
        private const val EXTRA_ORDER_NUMBER = "extra_order_number"

        fun start(context: Context, orderNumber: String) {
            val intent = Intent(context, UpdateOrderStatusActivity::class.java)
                .putExtra(EXTRA_ORDER_NUMBER, orderNumber)
            context.startActivity(intent)
        }
    }
}

/** Estados a los que puede pasar el pedido desde "En preparación". */
private enum class TargetStatus(val label: String, val description: String, val icon: ImageVector) {
    PACKED("Empacado", "El producto está empacado y listo para ser recogido", Icons.Outlined.Inventory2),
    DISPATCHED("Despachado", "Entregado al transportador", Icons.Outlined.LocalShipping)
}

/** Sugerencias de transportadoras. En producción vendrá del catálogo. */
private val CARRIER_OPTIONS: List<Pair<String, String>> = listOf(
    "SERVIENTREGA_PREMIUM" to "Servientrega Premium",
    "COORDINADORA" to "Coordinadora",
    "ENVIA" to "Envia",
    "INTERRAPIDISIMO" to "Interrapidísimo"
)

@Composable
fun UpdateOrderStatusScreen(
    modifier: Modifier = Modifier,
    orderNumber: String,
    onDismiss: () -> Unit,
    onConfirm: (newStatus: String) -> Unit
) {
    var selected by remember { mutableStateOf(TargetStatus.DISPATCHED) }
    var carrierKey by remember { mutableStateOf(CARRIER_OPTIONS.first().first) }
    var trackingNumber by remember { mutableStateOf("") }
    val carrierLabel = CARRIER_OPTIONS.firstOrNull { it.first == carrierKey }?.second

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = BrandSpacing.lg, vertical = BrandSpacing.md)
    ) {
        // Header con título + X
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Actualizar estado",
                    style = TextStyle(
                        fontFamily = FontFamily.Serif,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandColors.TextPrimary
                    )
                )
                Text(
                    text = "ORDEN #$orderNumber",
                    color = BrandColors.TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
            }
            IconButton(onClick = onDismiss) {
                Icon(Icons.Outlined.Close, contentDescription = "Cerrar", tint = BrandColors.TextSecondary)
            }
        }

        Spacer(modifier = Modifier.height(BrandSpacing.md))

        // Estado actual + badge ACTIVO
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
                .padding(BrandSpacing.md)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Estado actual", color = BrandColors.TextSecondary, fontSize = 12.sp)
                    Text(
                        text = "En preparación",
                        color = BrandColors.TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Box(
                    modifier = Modifier
                        .background(BrandColors.FarmerPrimary, RoundedCornerShape(50))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "ACTIVO",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(BrandSpacing.md))

        // Cards de los siguientes estados
        TargetStatus.entries.forEach { status ->
            StatusOptionCard(
                status = status,
                isSelected = selected == status,
                onClick = { selected = status }
            )
            Spacer(modifier = Modifier.height(BrandSpacing.sm))
        }

        // Si "Despachado" está seleccionado, mostrar campos de transportador y guía.
        if (selected == TargetStatus.DISPATCHED) {
            Spacer(modifier = Modifier.height(BrandSpacing.xs))
            LabeledDropdown(
                label = "TRANSPORTADOR",
                selectedLabel = carrierLabel,
                placeholder = "Seleccionar",
                options = CARRIER_OPTIONS,
                onSelect = { key, _ -> carrierKey = key }
            )
            Spacer(modifier = Modifier.height(BrandSpacing.sm))
            LabeledTextField(
                label = "NÚMERO DE GUÍA",
                value = trackingNumber,
                onValueChange = { trackingNumber = it },
                placeholder = "Ej: 1234-5678-9012"
            )
        }

        Spacer(modifier = Modifier.height(BrandSpacing.lg))

        Button(
            onClick = { onConfirm(selected.label) },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BrandColors.FarmerPrimary,
                contentColor = Color.White
            )
        ) {
            Text(text = "Confirmar cambio", fontWeight = FontWeight.SemiBold)
        }
        TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Cancelar", color = BrandColors.TextSecondary, fontSize = 13.sp)
        }
    }
}

@Composable
private fun StatusOptionCard(
    status: TargetStatus,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) BrandColors.FarmerPrimary else Color.Transparent
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .border(width = 2.dp, color = borderColor, shape = RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(BrandSpacing.md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = if (isSelected) BrandColors.FarmerPrimary else BrandColors.InputBackground,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = status.icon,
                contentDescription = null,
                tint = if (isSelected) Color.White else BrandColors.TextPrimary,
                modifier = Modifier.size(22.dp)
            )
        }
        Column(modifier = Modifier.weight(1f).padding(start = BrandSpacing.sm)) {
            Text(
                text = status.label,
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BrandColors.TextPrimary
                )
            )
            Text(
                text = status.description,
                color = BrandColors.TextSecondary,
                fontSize = 12.sp
            )
        }
    }
}
