package com.cafeteros.historia.ui.features.farmer_sales

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme

// ── Datos mock del feature ──────────────────────────────────────────────────

private data class CarrierOption(
    val id: String,
    val name: String,
    val priceCop: Int,
    val estimatedDeliveryDays: String,
    val isRecommended: Boolean = false
)

private val MOCK_CARRIERS = listOf(
    CarrierOption("coordinadora", "Coordinadora", 12_000, "2-3 días hábiles", isRecommended = true),
    CarrierOption("servientrega", "Servientrega", 13_500, "2-4 días hábiles"),
    CarrierOption("envia", "Envia", 11_900, "4 días hábiles"),
    CarrierOption("interrapidisimo", "Interrapidísimo", 14_200, "1-2 días hábiles")
)

private enum class PickupMethod(val label: String, val description: String) {
    DROP_OFF("Llevar al punto", "Entrega en el punto u oficina más cercana"),
    PICKUP("Solicitar recolección", "Un mensajero pasará por tu finca mañana")
}

class GenerateShippingGuideActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val orderNumber = intent.getStringExtra(EXTRA_ORDER_NUMBER) ?: "—"

        setContent {
            CafeterosTheme {
                GenerateShippingGuideScreen(
                    orderNumber = orderNumber,
                    onBack = ::finish,
                    onGenerate = { carrierId ->
                        ShippingGuideGeneratedActivity.start(this, orderNumber, carrierId)
                        finish()
                    }
                )
            }
        }
    }

    companion object {
        private const val EXTRA_ORDER_NUMBER = "extra_order_number"

        fun start(context: Context, orderNumber: String) {
            val intent = Intent(context, GenerateShippingGuideActivity::class.java)
                .putExtra(EXTRA_ORDER_NUMBER, orderNumber)
            context.startActivity(intent)
        }
    }
}

/**
 * Pantalla "Generar guía de envío": header con datos del pedido,
 * selector de transportador, info del paquete, método de recogida, mapa
 * placeholder y resumen de liquidación.
 */
@Composable
fun GenerateShippingGuideScreen(
    modifier: Modifier = Modifier,
    orderNumber: String,
    onBack: () -> Unit,
    onGenerate: (carrierId: String) -> Unit
) {
    var selectedCarrierId by remember { mutableStateOf(MOCK_CARRIERS.first().id) }
    var insureShipment by remember { mutableStateOf(true) }
    var pickupMethod by remember { mutableStateOf(PickupMethod.PICKUP) }

    val selectedCarrier = MOCK_CARRIERS.first { it.id == selectedCarrierId }
    val insurance = if (insureShipment) 3_000 else 0
    val totalToPay = selectedCarrier.priceCop + insurance - 12_000  // Resta del cobro al comprador

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding()
    ) {
        TopBar(onBack = onBack)

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = BrandSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
        ) {
            Spacer(modifier = Modifier.height(BrandSpacing.xs))
            OrderConfirmedCard(orderNumber = orderNumber)

            CarrierSelector(
                carriers = MOCK_CARRIERS,
                selectedId = selectedCarrierId,
                onSelect = { selectedCarrierId = it }
            )

            PackageInfoCard(insureShipment = insureShipment, onToggleInsurance = { insureShipment = it })

            PickupMethodSection(selected = pickupMethod, onSelect = { pickupMethod = it })

            // Placeholder de mapa con origen
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(Color(0xFFE8E2D5), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = null,
                        tint = BrandColors.FarmerPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Text(
                    text = "ORIGEN: HUILA, COLOMBIA",
                    color = BrandColors.TextSecondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
            }

            LiquidationSummary(
                carrierName = selectedCarrier.name,
                shippingCost = selectedCarrier.priceCop,
                insurance = insurance,
                estimatedDate = "Jueves, 23 de octubre",
                totalToPay = totalToPay
            )

            Spacer(modifier = Modifier.height(BrandSpacing.md))
            Button(
                onClick = { onGenerate(selectedCarrierId) },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandColors.FarmerPrimary,
                    contentColor = Color.White
                )
            ) {
                Icon(Icons.Outlined.LocalShipping, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.size(8.dp))
                Text(text = "Generar guía de envío", fontWeight = FontWeight.SemiBold)
            }
            Spacer(modifier = Modifier.height(BrandSpacing.lg))
        }
    }
}

@Composable
private fun TopBar(onBack: () -> Unit) {
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
            text = "Generar guía de envío",
            modifier = Modifier.weight(1f),
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = BrandColors.TextPrimary
            )
        )
    }
}

@Composable
private fun OrderConfirmedCard(orderNumber: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CoffeeBrown, RoundedCornerShape(12.dp))
            .padding(BrandSpacing.md),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "ORDEN CONFIRMADA",
            color = BrandColors.CreamWhiteMuted,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp
        )
        Text(
            text = "Pedido #$orderNumber",
            color = BrandColors.CreamWhite,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "COMPRADOR", color = BrandColors.CreamWhiteMuted, fontSize = 9.sp)
                Text(text = "María G.", color = BrandColors.CreamWhite, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
            Column {
                Text(text = "DESTINO", color = BrandColors.CreamWhiteMuted, fontSize = 9.sp)
                Text(text = "Bogotá, D.C.", color = BrandColors.CreamWhite, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
private fun CarrierSelector(
    carriers: List<CarrierOption>,
    selectedId: String,
    onSelect: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Seleccionar Transportador",
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BrandColors.TextPrimary
                )
            )
            Box(
                modifier = Modifier
                    .background(BrandColors.IndicatorInactive, RoundedCornerShape(50))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "${carriers.size} opciones disponibles",
                    color = BrandColors.TextPrimary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        carriers.forEach { carrier ->
            CarrierCard(
                carrier = carrier,
                isSelected = carrier.id == selectedId,
                onClick = { onSelect(carrier.id) }
            )
        }
    }
}

@Composable
private fun CarrierCard(
    carrier: CarrierOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = when {
        isSelected -> BrandColors.FarmerPrimary
        carrier.isRecommended -> Color(0xFFC9A24A)
        else -> Color.Transparent
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .border(width = 2.dp, color = borderColor, shape = RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(BrandSpacing.md)
    ) {
        if (carrier.isRecommended) {
            Box(
                modifier = Modifier
                    .background(Color(0xFFC9A24A), RoundedCornerShape(50))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "RECOMENDADO",
                    color = Color.White,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(BrandColors.InputBackground, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.LocalShipping,
                    contentDescription = null,
                    tint = BrandColors.TextPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
            Column(modifier = Modifier.weight(1f).padding(start = BrandSpacing.sm)) {
                Text(
                    text = carrier.name,
                    color = BrandColors.TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Entrega estimada: ${carrier.estimatedDeliveryDays}",
                    color = BrandColors.TextSecondary,
                    fontSize = 11.sp
                )
            }
            Text(
                text = "\$" + "%,d".format(carrier.priceCop).replace(',', '.'),
                color = BrandColors.TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun PackageInfoCard(insureShipment: Boolean, onToggleInsurance: (Boolean) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Información del Paquete",
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BrandColors.TextPrimary
                )
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Edit, contentDescription = null, tint = BrandColors.FarmerPrimary, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.size(4.dp))
                Text(text = "Editar", color = BrandColors.FarmerPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
                .padding(BrandSpacing.md),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            InfoRow(label = "Peso Total", value = "5.0 kg")
            InfoRow(label = "Dimensiones", value = "30 × 20 × 15 cm")
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(BrandColors.DividerLine))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Asegurar envío", color = BrandColors.TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    Text(text = "+ \$3.000", color = BrandColors.TextSecondary, fontSize = 12.sp)
                }
                Switch(
                    checked = insureShipment,
                    onCheckedChange = onToggleInsurance,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFFC9A24A),
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = BrandColors.IndicatorInactive
                    )
                )
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = BrandColors.TextSecondary, fontSize = 13.sp)
        Text(text = value, color = BrandColors.TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun PickupMethodSection(selected: PickupMethod, onSelect: (PickupMethod) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)) {
        Text(
            text = "Método de Recogida",
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = BrandColors.TextPrimary
            )
        )
        PickupMethod.entries.forEach { method ->
            PickupMethodRow(
                method = method,
                isSelected = method == selected,
                onClick = { onSelect(method) }
            )
        }
    }
}

@Composable
private fun PickupMethodRow(method: PickupMethod, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) BrandColors.FarmerPrimary else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(BrandSpacing.md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(selectedColor = BrandColors.FarmerPrimary)
        )
        Column(modifier = Modifier.weight(1f).padding(start = 4.dp)) {
            Text(text = method.label, color = BrandColors.TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Text(text = method.description, color = BrandColors.TextSecondary, fontSize = 12.sp)
        }
    }
}

@Composable
private fun LiquidationSummary(
    carrierName: String,
    shippingCost: Int,
    insurance: Int,
    estimatedDate: String,
    totalToPay: Int
) {
    Column(verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)) {
        Text(
            text = "Resumen de Liquidación",
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = BrandColors.TextPrimary
            )
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
                .padding(BrandSpacing.md),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            InfoRow(label = "Transportador", value = carrierName)
            InfoRow(label = "Costo Envío", value = "\$" + "%,d".format(shippingCost).replace(',', '.'))
            InfoRow(label = "Seguro Adicional", value = "\$" + "%,d".format(insurance).replace(',', '.'))
            InfoRow(label = "Fecha Estimada", value = estimatedDate)
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(BrandColors.DividerLine))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Total a pagar:", color = BrandColors.TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = "\$" + "%,d".format(totalToPay).replace(',', '.'),
                    color = BrandColors.FarmerPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
