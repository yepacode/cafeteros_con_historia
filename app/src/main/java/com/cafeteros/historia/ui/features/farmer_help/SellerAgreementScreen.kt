package com.cafeteros.historia.ui.features.farmer_help

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme

class SellerAgreementActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                SellerAgreementScreen(
                    onBack = ::finish,
                    onDownload = { Toast.makeText(this, "Próximamente: descargar PDF", Toast.LENGTH_SHORT).show() }
                )
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, SellerAgreementActivity::class.java))
        }
    }
}

@Composable
fun SellerAgreementScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onDownload: () -> Unit
) {
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
                text = "Acuerdo del Vendedor",
                modifier = Modifier.weight(1f),
                style = TextStyle(fontFamily = FontFamily.Serif, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, fontStyle = FontStyle.Italic, color = BrandColors.TextPrimary),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
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
            Box(
                modifier = Modifier
                    .background(Color(0xFFFFF1B8), RoundedCornerShape(50))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(text = "VERSIÓN V2.0", color = Color(0xFF8C6E1F), fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp)
            }

            Text(
                text = "Acuerdo del Vendedor",
                style = TextStyle(fontFamily = FontFamily.Serif, fontSize = 32.sp, fontWeight = FontWeight.Bold, color = BrandColors.TextPrimary, lineHeight = 36.sp)
            )
            Text(text = "Última actualización: 15 de abril de 2026", color = BrandColors.TextSecondary, fontSize = 11.sp, fontStyle = FontStyle.Italic)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(Color(0xFFC9A24A), RoundedCornerShape(8.dp))
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
                    .padding(BrandSpacing.md),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.AutoMirrored.Outlined.MenuBook, contentDescription = null, tint = BrandColors.TextPrimary, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.size(6.dp))
                    Text(
                        text = "Índice del Acuerdo",
                        style = TextStyle(fontFamily = FontFamily.Serif, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BrandColors.TextPrimary)
                    )
                }
                IndexEntry(number = "01", title = "Objeto del acuerdo")
                IndexEntry(number = "02", title = "Requisitos para ser caficultor")
                IndexEntry(number = "04", title = "Comisiones y pagos", isHighlight = true)
                Text(
                    text = "Ver todos los 14 puntos",
                    color = BrandColors.FarmerPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            ClauseHeader(number = "1.", title = "Objeto del acuerdo")
            Text(
                text = "El presente Acuerdo del Vendedor establece los términos y condiciones que rigen la relación entre El Caficultor (en adelante, \"la Plataforma\") y el productor independiente (en adelante, el \"Vendedor\").",
                color = BrandColors.TextPrimary,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
            Text(
                text = "Al registrarse como vendedor, usted acepta que la plataforma actúa como un mercado editorial y comercial para conectar el origen del café con el consumidor final.",
                color = BrandColors.TextPrimary,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFF1B8), RoundedCornerShape(8.dp))
                    .padding(BrandSpacing.md),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = "CLÁUSULA CRÍTICA", color = Color(0xFF8C6E1F), fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp)
                Text(
                    text = "\"La autenticidad del origen es nuestra mayor promesa. Cualquier falsedad en los datos de la finca resultará en la terminación inmediata.\"",
                    color = BrandColors.TextPrimary,
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic,
                    lineHeight = 16.sp
                )
            }

            ClauseHeader(number = "2.", title = "Requisitos del Caficultor")
            Text(
                text = "Para mantener los estándares de La Crónica del Origen, todo vendedor debe:",
                color = BrandColors.TextPrimary,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
            RequirementRow(text = "Poseer un registro vigente ante la Federación Nacional de Cafeteros.")
            RequirementRow(text = "Cumplir con las normas de sostenibilidad ambiental locales.")
            RequirementRow(text = "Proveer material visual (fotografía) de alta calidad de su cultivo.")

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BrandColors.CoffeeBrown, RoundedCornerShape(12.dp))
                    .padding(BrandSpacing.md),
                verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
            ) {
                Text(text = "4. Comisiones y Pagos", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif)
                Text(
                    text = "Estructura transparente de rentabilidad para el productor.",
                    color = BrandColors.CreamWhiteMuted,
                    fontSize = 11.sp
                )
                Text(
                    text = "8%",
                    color = Color(0xFFC9A24A),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )
                Text(text = "COMISIÓN POR VENTA", color = BrandColors.CreamWhiteMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp)
                Row(modifier = Modifier.padding(top = 4.dp), horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)) {
                    DarkInfoTile(modifier = Modifier.weight(1f), label = "PAGO CICLO", value = "Cada 15 días")
                    DarkInfoTile(modifier = Modifier.weight(1f), label = "TRANSFERENCIA", value = "ACH Directo")
                }
            }

            ClauseHeader(number = "6.", title = "Calidad de Productos")
            Text(
                text = "Solo se permiten cafés con un puntaje de taza superior a 84 puntos (SCA). El vendedor se compromete a enviar muestras trimestrales para control de calidad en nuestros laboratorios aliados.",
                color = BrandColors.TextPrimary,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE8F4EC), RoundedCornerShape(50))
                    .padding(horizontal = BrandSpacing.md, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Outlined.Verified, contentDescription = null, tint = BrandColors.FarmerPrimary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.size(6.dp))
                Text(text = "Aceptaste esta versión el 16 abr 2026", color = BrandColors.FarmerPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(BrandSpacing.md))
            Button(
                onClick = onDownload,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandColors.CoffeeBrown, contentColor = Color.White)
            ) {
                Icon(Icons.Outlined.Download, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.size(8.dp))
                Text(text = "Descargar Acuerdo PDF", fontWeight = FontWeight.SemiBold)
            }

            Text(
                text = "© 2024 EL CAFICULTOR. LA CRÓNICA DEL ORIGEN.",
                color = BrandColors.TextSecondary,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp,
                modifier = Modifier.fillMaxWidth().padding(top = BrandSpacing.sm),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(text = "TÉRMINOS", color = BrandColors.TextPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.size(BrandSpacing.md))
                Text(text = "PRIVACIDAD", color = BrandColors.TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(BrandSpacing.lg))
        }
    }
}

@Composable
private fun IndexEntry(number: String, title: String, isHighlight: Boolean = false) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = number,
            color = if (isHighlight) Color(0xFFC9A24A) else BrandColors.TextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = title,
            color = if (isHighlight) Color(0xFFC9A24A) else BrandColors.TextPrimary,
            fontSize = 12.sp,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
private fun ClauseHeader(number: String, title: String) {
    Text(
        text = "$number $title",
        style = TextStyle(fontFamily = FontFamily.Serif, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = BrandColors.TextPrimary),
        modifier = Modifier.padding(top = BrandSpacing.sm)
    )
}

@Composable
private fun RequirementRow(text: String) {
    Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(vertical = 4.dp)) {
        Icon(Icons.Default.Check, contentDescription = null, tint = BrandColors.FarmerPrimary, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.size(8.dp))
        Text(text = text, color = BrandColors.TextPrimary, fontSize = 13.sp, lineHeight = 17.sp)
    }
}

@Composable
private fun DarkInfoTile(modifier: Modifier = Modifier, label: String, value: String) {
    Column(
        modifier = modifier
            .background(Color(0xFF2A1B17), RoundedCornerShape(8.dp))
            .padding(BrandSpacing.sm),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.Payments, contentDescription = null, tint = BrandColors.CreamWhiteMuted, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.size(4.dp))
            Text(text = label, color = BrandColors.CreamWhiteMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
        }
        Text(text = value, color = BrandColors.CreamWhite, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}
