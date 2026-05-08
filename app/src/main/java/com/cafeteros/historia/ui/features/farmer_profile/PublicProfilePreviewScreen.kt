package com.cafeteros.historia.ui.features.farmer_profile

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Coffee
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material.icons.outlined.WbSunny
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme

class PublicProfilePreviewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                PublicProfilePreviewScreen(
                    onClose = ::finish,
                    onEdit = {
                        EditFarmActivity.start(this)
                        finish()
                    }
                )
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, PublicProfilePreviewActivity::class.java))
        }
    }
}

@Composable
fun PublicProfilePreviewScreen(
    modifier: Modifier = Modifier,
    onClose: () -> Unit,
    onEdit: () -> Unit
) {
    Box(modifier = modifier.fillMaxSize().background(BrandColors.AuthBackground).systemBarsPadding()) {
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(bottom = 96.dp)) {
            // Top bar
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = BrandSpacing.sm, vertical = BrandSpacing.xs),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onClose) {
                    Icon(Icons.Outlined.Close, contentDescription = "Cerrar", tint = BrandColors.TextPrimary)
                }
                Text(
                    text = "Vista previa",
                    modifier = Modifier.weight(1f),
                    style = TextStyle(fontFamily = FontFamily.Serif, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, fontStyle = FontStyle.Italic, color = BrandColors.TextPrimary),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                TextButton(onClick = onEdit) {
                    Text(text = "Editar", color = BrandColors.FarmerPrimary, fontWeight = FontWeight.Bold)
                }
            }

            // Banner amarillo
            Row(
                modifier = Modifier
                    .padding(horizontal = BrandSpacing.lg)
                    .fillMaxWidth()
                    .background(Color(0xFFFFF1B8), RoundedCornerShape(12.dp))
                    .padding(BrandSpacing.md),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Outlined.Info, contentDescription = null, tint = Color(0xFF8C6E1F), modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "Así te ven los compradores. ¿Algo para mejorar? Toca Editar.",
                    color = BrandColors.TextPrimary,
                    fontSize = 12.sp,
                    lineHeight = 15.sp
                )
            }

            // Hero con paisaje + avatar grande
            Spacer(modifier = Modifier.height(BrandSpacing.md))
            Box(
                modifier = Modifier.fillMaxWidth().height(180.dp).padding(horizontal = BrandSpacing.lg)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(BrandColors.FarmerPrimary, RoundedCornerShape(16.dp))
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = 44.dp)
                        .size(96.dp)
                        .background(Color.White, CircleShape)
                        .padding(4.dp)
                        .background(BrandColors.CoffeeBrown, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Outlined.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(56.dp))
                }
            }
            Spacer(modifier = Modifier.height(56.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Don Alberto Ramírez",
                    style = TextStyle(fontFamily = FontFamily.Serif, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = BrandColors.TextPrimary)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.LocationOn, contentDescription = null, tint = BrandColors.FarmerPrimary, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(text = "Finca La Esperanza", color = BrandColors.TextSecondary, fontSize = 13.sp)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    PreviewBadge(label = "VERIFICADO", bg = Color(0xFFE8F4EC), fg = BrandColors.FarmerPrimary)
                    PreviewBadge(label = "HUILA", bg = Color(0xFFC9A24A), fg = Color.White)
                    PreviewBadge(label = "DESDE 2026", bg = BrandColors.IndicatorInactive, fg = BrandColors.TextPrimary)
                }
            }

            Spacer(modifier = Modifier.height(BrandSpacing.lg))

            // Sección legado
            Column(modifier = Modifier.padding(horizontal = BrandSpacing.lg)) {
                Text(text = "LEGADO FAMILIAR", color = BrandColors.TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "La historia de Don Alberto",
                    style = TextStyle(fontFamily = FontFamily.Serif, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = BrandColors.TextPrimary, lineHeight = 28.sp)
                )
                Spacer(modifier = Modifier.height(BrandSpacing.sm))

                // Cita
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BrandColors.CardBackground, RoundedCornerShape(8.dp))
                        .padding(BrandSpacing.md)
                ) {
                    Text(
                        text = "\"Cada grano lleva 40 años de tradición y el alma de nuestra tierra.\"",
                        color = BrandColors.TextPrimary,
                        fontSize = 14.sp,
                        fontStyle = FontStyle.Italic,
                        fontFamily = FontFamily.Serif,
                        lineHeight = 20.sp
                    )
                }
                Spacer(modifier = Modifier.height(BrandSpacing.sm))
                Text(
                    text = "En las faldas de la cordillera central, Don Alberto ha cultivado la excelencia desde que era un niño. Su finca, La Esperanza, es más que tierra; es un santuario donde la biodiversidad y el café conviven en perfecta armonía. Con técnicas artesanales heredadas, cada cosecha es un tributo a la paciencia.",
                    color = BrandColors.TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }

            // Video placeholder
            Spacer(modifier = Modifier.height(BrandSpacing.md))
            Box(
                modifier = Modifier
                    .padding(horizontal = BrandSpacing.lg)
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(BrandColors.CoffeeBrown, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.PlayCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(56.dp))
            }

            // Proceso artesanal
            Spacer(modifier = Modifier.height(BrandSpacing.md))
            Column(modifier = Modifier.padding(horizontal = BrandSpacing.lg)) {
                Text(text = "PROCESO ARTESANAL", color = BrandColors.TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Spacer(modifier = Modifier.height(BrandSpacing.sm))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ProcessStep(icon = Icons.Outlined.Coffee, label = "COSECHA")
                    ProcessStep(icon = Icons.Outlined.WaterDrop, label = "DESPULPADO")
                    ProcessStep(icon = Icons.Outlined.WaterDrop, label = "FERMENTACIÓN")
                    ProcessStep(icon = Icons.Outlined.WbSunny, label = "SECADO")
                }
            }

            // Variedades
            Spacer(modifier = Modifier.height(BrandSpacing.md))
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = BrandSpacing.lg),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Nuestras Variedades",
                    style = TextStyle(fontFamily = FontFamily.Serif, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = BrandColors.TextPrimary)
                )
                Text(text = "Ver todos", color = BrandColors.FarmerPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
            Spacer(modifier = Modifier.height(BrandSpacing.sm))
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = BrandSpacing.lg),
                horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
            ) {
                VarietyCard(modifier = Modifier.weight(1f), name = "Caturra Lavado", profile = "Perfil cítrico, dulce", price = "\$45.000 COP")
                VarietyCard(modifier = Modifier.weight(1f), name = "Bourbon Rosado", profile = "Notas florales, miel", price = "\$58.000 COP")
            }

            // Lo que dicen
            Spacer(modifier = Modifier.height(BrandSpacing.md))
            Column(modifier = Modifier.padding(horizontal = BrandSpacing.lg)) {
                Text(
                    text = "Lo que dicen de nosotros",
                    style = TextStyle(fontFamily = FontFamily.Serif, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = BrandColors.TextPrimary)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    repeat(5) { Icon(Icons.Outlined.Star, contentDescription = null, tint = Color(0xFFC9A24A), modifier = Modifier.size(14.dp)) }
                    Spacer(modifier = Modifier.size(6.dp))
                    Text(text = "Ana M.", color = BrandColors.TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "\"El café más fresco que he probado. Se nota el amor en cada grano. La entrega fue perfecta.\"",
                    color = BrandColors.TextSecondary,
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic,
                    lineHeight = 16.sp
                )
            }
        }

        // Bottom bar con CTAs
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(BrandSpacing.md)
                .background(BrandColors.CoffeeBrown, RoundedCornerShape(50))
                .padding(horizontal = BrandSpacing.md, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "💬 ", fontSize = 16.sp)
            Text(
                text = "Enviar mensaje",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier.size(36.dp).background(BrandColors.FarmerPrimary, CircleShape).clickable(onClick = onEdit),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.Edit, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
private fun PreviewBadge(label: String, bg: Color, fg: Color) {
    Box(
        modifier = Modifier.background(bg, RoundedCornerShape(50)).padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(text = label, color = fg, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
    }
}

@Composable
private fun ProcessStep(icon: ImageVector, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Box(
            modifier = Modifier.size(40.dp).background(Color(0xFFE8F4EC), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = BrandColors.FarmerPrimary, modifier = Modifier.size(20.dp))
        }
        Text(text = label, color = BrandColors.TextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
    }
}

@Composable
private fun VarietyCard(modifier: Modifier = Modifier, name: String, profile: String, price: String) {
    Column(
        modifier = modifier
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .padding(BrandSpacing.sm)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().height(100.dp).background(BrandColors.InputBackground, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) { Text(text = "📷", fontSize = 28.sp) }
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = name, color = BrandColors.TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.Serif)
        Text(text = profile, color = BrandColors.TextSecondary, fontSize = 10.sp)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = price, color = BrandColors.TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
    }
}
