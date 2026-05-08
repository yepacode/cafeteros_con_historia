package com.cafeteros.historia.ui.features.farmer_wallet

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
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
import com.cafeteros.historia.ui.features.farmer_sales.SalesActivity
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme

class WithdrawConfirmationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val amount = intent.getIntExtra(EXTRA_AMOUNT, 0)
        setContent {
            CafeterosTheme {
                WithdrawConfirmationScreen(
                    amountCop = amount,
                    onDismiss = ::finish,
                    onSeeWallet = {
                        WalletActivity.start(this)
                        finish()
                    },
                    onSeeOrders = {
                        SalesActivity.start(this)
                        finish()
                    }
                )
            }
        }
    }

    companion object {
        private const val EXTRA_AMOUNT = "extra_amount"
        fun start(context: Context, amountCop: Int) {
            context.startActivity(
                Intent(context, WithdrawConfirmationActivity::class.java).putExtra(EXTRA_AMOUNT, amountCop)
            )
        }
    }
}

@Composable
fun WithdrawConfirmationScreen(
    modifier: Modifier = Modifier,
    amountCop: Int,
    onDismiss: () -> Unit,
    onSeeWallet: () -> Unit,
    onSeeOrders: () -> Unit
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
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            IconButton(onClick = onDismiss) {
                Icon(Icons.Outlined.Close, contentDescription = "Cerrar", tint = BrandColors.TextSecondary)
            }
        }

        Box(
            modifier = Modifier.size(80.dp).background(Color(0xFFC9A24A), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.AccountBalance,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
        }
        Spacer(modifier = Modifier.height(BrandSpacing.sm))
        Text(
            text = "¡Retiro solicitado!",
            style = TextStyle(fontFamily = FontFamily.Serif, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = BrandColors.TextPrimary)
        )
        Text(text = "Tu dinero está en camino", color = BrandColors.TextSecondary, fontSize = 13.sp)

        Spacer(modifier = Modifier.height(BrandSpacing.md))

        // Card resumen
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
                .padding(BrandSpacing.md),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Retiro de \$" + "%,d".format(amountCop).replace(',', '.'),
                color = BrandColors.TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif
            )
            Text(text = "🏦 Bancolombia Ahorros **** 8754", color = BrandColors.TextSecondary, fontSize = 12.sp)

            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(BrandColors.DividerLine))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(text = "REFERENCIA", color = BrandColors.TextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text(text = "RT-98734", color = BrandColors.TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "LLEGADA ESTIMADA", color = BrandColors.TextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Miércoles 22 abr", color = BrandColors.TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        Spacer(modifier = Modifier.height(BrandSpacing.md))

        // Timeline de pasos
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
                .padding(BrandSpacing.md),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TimelineRow(label = "Solicitud recibida", subtitle = "Hoy 11:30 AM", state = StepState.DONE)
            TimelineRow(label = "Procesando", subtitle = "En curso", state = StepState.IN_PROGRESS)
            TimelineRow(label = "Transferencia enviada al banco", subtitle = null, state = StepState.PENDING)
            TimelineRow(label = "Dinero disponible en tu cuenta", subtitle = null, state = StepState.PENDING)
        }

        Spacer(modifier = Modifier.height(BrandSpacing.sm))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFF1B8), RoundedCornerShape(50))
                .padding(horizontal = BrandSpacing.md, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(Icons.Outlined.Notifications, contentDescription = null, tint = Color(0xFF8C6E1F), modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = "Te notificaremos cuando el dinero llegue",
                color = Color(0xFF8C6E1F),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(BrandSpacing.lg))
        Button(
            onClick = onSeeWallet,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BrandColors.CoffeeBrown,
                contentColor = Color.White
            )
        ) {
            Text(text = "Ver mi billetera", fontWeight = FontWeight.SemiBold)
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(
            onClick = onSeeOrders,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Ver mis pedidos", color = BrandColors.TextPrimary, fontWeight = FontWeight.SemiBold)
        }
    }
}

private enum class StepState { DONE, IN_PROGRESS, PENDING }

@Composable
private fun TimelineRow(label: String, subtitle: String?, state: StepState) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        when (state) {
            StepState.DONE -> Box(
                modifier = Modifier.size(20.dp).background(BrandColors.FarmerPrimary, CircleShape),
                contentAlignment = Alignment.Center
            ) { Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp)) }
            StepState.IN_PROGRESS -> Box(
                modifier = Modifier.size(20.dp).background(Color(0xFFC9A24A), CircleShape)
            )
            StepState.PENDING -> Icon(
                imageVector = Icons.Outlined.RadioButtonUnchecked,
                contentDescription = null,
                tint = BrandColors.IndicatorInactive,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.size(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                color = if (state == StepState.PENDING) BrandColors.TextSecondary else BrandColors.TextPrimary,
                fontSize = 13.sp,
                fontWeight = if (state == StepState.PENDING) FontWeight.Normal else FontWeight.SemiBold
            )
            if (subtitle != null) {
                Text(text = subtitle, color = BrandColors.TextSecondary, fontSize = 11.sp)
            }
        }
    }
}
