package com.cafeteros.historia.ui.features.farmer_wallet

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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme

private const val AVAILABLE_BALANCE = 4_820_500
private const val MIN_WITHDRAW = 50_000

private enum class TransferSpeed(val label: String, val description: String, val feeCop: Int) {
    NORMAL("Transferir ahora", "Llega en 1-2 días hábiles", 0),
    INSTANT("Transferencia inmediata", "Llega en minutos · Cargo \$2.500", 2_500)
}

class WithdrawActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                WithdrawScreen(
                    onBack = ::finish,
                    onConfirm = { amount ->
                        WithdrawConfirmationActivity.start(this, amount)
                        finish()
                    }
                )
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, WithdrawActivity::class.java))
        }
    }
}

@Composable
fun WithdrawScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onConfirm: (amountCop: Int) -> Unit
) {
    var amountInput by remember { mutableStateOf("500000") }
    var speed by remember { mutableStateOf(TransferSpeed.NORMAL) }

    val amount = amountInput.toIntOrNull() ?: 0
    val remaining = (AVAILABLE_BALANCE - amount).coerceAtLeast(0)
    val fee = speed.feeCop
    val toReceive = (amount - fee).coerceAtLeast(0)
    val canConfirm = amount in MIN_WITHDRAW..AVAILABLE_BALANCE

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = BrandSpacing.sm, vertical = BrandSpacing.xs),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Volver", tint = BrandColors.TextPrimary)
            }
            Text(
                text = "Retirar dinero",
                modifier = Modifier.weight(1f),
                style = TextStyle(fontFamily = FontFamily.Serif, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = BrandColors.TextPrimary)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = BrandSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
        ) {
            // Disponible
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
                    .padding(BrandSpacing.md)
            ) {
                Text(text = "Disponible para retirar", color = BrandColors.TextSecondary, fontSize = 12.sp)
                Text(
                    text = "\$" + "%,d".format(AVAILABLE_BALANCE).replace(',', '.'),
                    color = BrandColors.TextPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )
                Text(
                    text = "Mínimo de retiro: \$" + "%,d".format(MIN_WITHDRAW).replace(',', '.'),
                    color = BrandColors.TextSecondary,
                    fontSize = 11.sp
                )
            }

            Text(
                text = "¿Cuánto quieres retirar?",
                style = TextStyle(fontFamily = FontFamily.Serif, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = BrandColors.TextPrimary)
            )

            // Input de monto grande
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
                    .padding(vertical = BrandSpacing.md, horizontal = BrandSpacing.md),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "$", color = BrandColors.TextPrimary, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.size(6.dp))
                    BasicTextField(
                        value = amountInput,
                        onValueChange = { amountInput = it.filter { ch -> ch.isDigit() } },
                        singleLine = true,
                        textStyle = TextStyle(
                            fontFamily = FontFamily.Serif,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrandColors.TextPrimary,
                            textAlign = TextAlign.Center
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }

            // Quick percent chips
            val pctChips: List<Pair<Int, String>> = listOf(
                25 to "25%",
                50 to "50%",
                75 to "75%",
                100 to "Todo"
            )
            Row(horizontalArrangement = Arrangement.spacedBy(BrandSpacing.xs)) {
                pctChips.forEach { (pct, label) ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(BrandColors.CardBackground, RoundedCornerShape(50))
                            .clickable { amountInput = (AVAILABLE_BALANCE * pct / 100).toString() }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = label, color = BrandColors.TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            Text(
                text = "Te quedarían: \$" + "%,d".format(remaining).replace(',', '.') + " en tu saldo",
                color = BrandColors.TextSecondary,
                fontSize = 12.sp
            )

            // Cuenta de destino
            Text(
                text = "Cuenta de destino",
                style = TextStyle(fontFamily = FontFamily.Serif, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = BrandColors.TextPrimary)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
                    .border(2.dp, BrandColors.FarmerPrimary, RoundedCornerShape(12.dp))
                    .padding(BrandSpacing.md),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(36.dp).background(Color(0xFFFFD23F), RoundedCornerShape(8.dp))
                )
                Column(modifier = Modifier.weight(1f).padding(horizontal = BrandSpacing.sm)) {
                    Text(text = "Bancolombia · Ahorros", color = BrandColors.TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Text(text = "**** 8754", color = BrandColors.TextSecondary, fontSize = 12.sp)
                    Text(text = "A nombre de Alberto Ramírez", color = BrandColors.TextSecondary, fontSize = 10.sp)
                }
                Box(
                    modifier = Modifier.size(20.dp).background(BrandColors.FarmerPrimary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "✓", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
            TextButton(onClick = { /* TODO usar otra cuenta */ }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Usar otra cuenta →", color = BrandColors.FarmerPrimary, fontSize = 12.sp)
            }

            // Velocidad
            TransferSpeed.entries.forEach { s ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
                        .border(
                            width = if (speed == s) 2.dp else 0.dp,
                            color = if (speed == s) BrandColors.FarmerPrimary else Color.Transparent,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { speed = s }
                        .padding(BrandSpacing.sm),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = speed == s,
                        onClick = { speed = s },
                        colors = RadioButtonDefaults.colors(selectedColor = BrandColors.FarmerPrimary)
                    )
                    Column(modifier = Modifier.weight(1f).padding(start = 4.dp)) {
                        Text(text = s.label, color = BrandColors.TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        Text(text = s.description, color = BrandColors.TextSecondary, fontSize = 11.sp)
                    }
                }
            }

            // Resumen
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
                    .padding(BrandSpacing.md),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SummaryRow("Monto a retirar", amount)
                SummaryRow("Cargo por transferencia", fee)
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(BrandColors.DividerLine))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "Recibirás:", color = BrandColors.TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = "\$" + "%,d".format(toReceive).replace(',', '.'),
                        color = BrandColors.FarmerPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                text = "Los retiros están sujetos a las políticas bancarias. Para montos superiores a \$10M se requiere validación adicional de identidad por motivos de seguridad.",
                color = BrandColors.TextSecondary,
                fontSize = 10.sp,
                lineHeight = 14.sp
            )

            Spacer(modifier = Modifier.height(BrandSpacing.sm))
            Button(
                onClick = { onConfirm(amount) },
                enabled = canConfirm,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFC9A24A),
                    contentColor = Color.White,
                    disabledContainerColor = BrandColors.IndicatorInactive
                )
            ) {
                Text(text = "Confirmar retiro", fontWeight = FontWeight.SemiBold)
            }
            TextButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Cancelar", color = BrandColors.TextSecondary, fontSize = 13.sp)
            }
            Spacer(modifier = Modifier.height(BrandSpacing.lg))
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: Int) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label, color = BrandColors.TextSecondary, fontSize = 12.sp)
        Text(
            text = "\$" + "%,d".format(value).replace(',', '.'),
            color = BrandColors.TextPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
