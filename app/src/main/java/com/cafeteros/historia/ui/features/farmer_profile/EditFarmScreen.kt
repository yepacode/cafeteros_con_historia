package com.cafeteros.historia.ui.features.farmer_profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.features.farmer_registration.components.LabeledTextField
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme

private enum class EditFarmTab(val label: String) {
    HISTORY("Historia"), PHOTOS("Fotos"), VIDEO("Video"), DATA("Datos"), PROCESS("Proceso")
}

class EditFarmActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                EditFarmScreen(
                    onClose = ::finish,
                    onSave = {
                        Toast.makeText(this, "Cambios guardados (próximamente con backend)", Toast.LENGTH_SHORT).show()
                        finish()
                    },
                    onAction = { action ->
                        Toast.makeText(this, "Próximamente: $action", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, EditFarmActivity::class.java))
        }
    }
}

@Composable
fun EditFarmScreen(
    modifier: Modifier = Modifier,
    onClose: () -> Unit,
    onSave: () -> Unit,
    onAction: (String) -> Unit
) {
    var activeTab by remember { mutableStateOf(EditFarmTab.HISTORY) }
    var title by remember { mutableStateOf("Finca La Esperanza: Tres generaciones de café") }
    var highlight by remember { mutableStateOf("El alma de la montaña en cada grano…") }
    var fullStory by remember { mutableStateOf("Nuestra historia comienza en 1954, cuando mi abuelo decidió que las laderas del Huila eran el lienzo perfecto para el mejor café del mundo. Hoy, seguimos honrando esa tradición, combinando métodos ancestrales con técnicas de fermentación controlada que realzan las notas cítricas y de chocolate que nos caracterizan.\n\nCada árbol es tratado como parte de la familia, cultivado a 1,800 metros sobre el nivel del mar, bajo la sombra de…") }
    var altitude by remember { mutableStateOf("1850") }
    var area by remember { mutableStateOf("12") }
    var varieties by remember { mutableStateOf("Caturra, Castillo, Geisha") }
    var shadeType by remember { mutableStateOf("Policultivo (Nativo)") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding()
    ) {
        // Top bar
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = BrandSpacing.sm, vertical = BrandSpacing.xs),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onClose) {
                Icon(Icons.Outlined.Close, contentDescription = "Cerrar", tint = BrandColors.TextPrimary)
            }
            Text(
                text = "Editar mi finca",
                modifier = Modifier.weight(1f),
                style = TextStyle(fontFamily = FontFamily.Serif, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = BrandColors.TextPrimary)
            )
            TextButton(onClick = onSave) {
                Text(text = "Guardar", color = BrandColors.FarmerPrimary, fontWeight = FontWeight.Bold)
            }
        }

        // Tabs
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = BrandSpacing.lg),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            EditFarmTab.entries.forEach { t ->
                val active = t == activeTab
                Box(
                    modifier = Modifier
                        .clickable { activeTab = t }
                        .padding(vertical = 8.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = t.label,
                            color = if (active) BrandColors.FarmerPrimary else BrandColors.TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = if (active) FontWeight.Bold else FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .size(width = 24.dp, height = 2.dp)
                                .background(if (active) BrandColors.FarmerPrimary else Color.Transparent)
                        )
                    }
                }
            }
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

            LabeledTextField(label = "TÍTULO DE TU HISTORIA", value = title, onValueChange = { title = it })
            LabeledTextField(label = "FRASE DESTACADA", value = highlight, onValueChange = { highlight = it })

            Text(text = "TU HISTORIA COMPLETA", color = BrandColors.TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BrandColors.InputBackground, RoundedCornerShape(12.dp))
                    .padding(BrandSpacing.md)
            ) {
                BasicTextField(
                    value = fullStory,
                    onValueChange = { fullStory = it },
                    textStyle = TextStyle(color = BrandColors.TextPrimary, fontSize = 13.sp, lineHeight = 18.sp),
                    modifier = Modifier.fillMaxWidth().height(180.dp)
                )
            }
            Text(
                text = "${fullStory.length} / 3.000 caracteres",
                color = BrandColors.TextSecondary,
                fontSize = 10.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.End
            )

            // IA helper
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFF1B8), RoundedCornerShape(12.dp))
                    .padding(BrandSpacing.md),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier.size(36.dp).background(Color(0xFFC9A24A), CircleShape),
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Outlined.AutoAwesome, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp)) }
                Text(
                    text = "Mejora tu historia con IA",
                    color = BrandColors.TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )
                Text(
                    text = "Nuestro asistente puede ayudarte a redactar una narrativa más cautivadora basada en tus datos actuales.",
                    color = BrandColors.TextSecondary,
                    fontSize = 11.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    lineHeight = 14.sp
                )
                Button(
                    onClick = { onAction("Sugerir mejoras IA") },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandColors.CoffeeBrown, contentColor = Color.White)
                ) {
                    Text(text = "Sugerir mejoras", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            // Galería de fotos
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "GALERÍA DE FOTOS", color = BrandColors.TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                Text(text = "Arrastra para reordenar", color = BrandColors.TextSecondary, fontSize = 10.sp, fontStyle = FontStyle.Italic)
            }
            Column(verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)) {
                Row(horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)) {
                    PhotoSlot(modifier = Modifier.weight(1f), isPrincipal = true, onClick = { onAction("Editar foto principal") })
                    PhotoSlot(modifier = Modifier.weight(1f), onClick = { onAction("Editar foto") })
                }
                Row(horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)) {
                    PhotoSlot(modifier = Modifier.weight(1f), onClick = { onAction("Editar foto") })
                    AddPhotoSlot(modifier = Modifier.weight(1f), onClick = { onAction("Agregar foto") })
                }
            }

            // Video
            Text(text = "VIDEO DE LA FINCA", color = BrandColors.TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(BrandColors.CoffeeBrown, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.PlayCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(40.dp))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)) {
                OutlinedButton(
                    onClick = { onAction("Reemplazar video") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(50)
                ) { Text(text = "🔄 Reemplazar", color = BrandColors.TextPrimary, fontSize = 12.sp) }
                OutlinedButton(
                    onClick = { onAction("Grabar video") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(50)
                ) { Text(text = "🎥 Grabar nuevo", color = BrandColors.TextPrimary, fontSize = 12.sp) }
            }

            // Datos técnicos
            Text(text = "DATOS TÉCNICOS DE LA FINCA", color = BrandColors.TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
            LabeledTextField(label = "ALTITUD PROMEDIO", value = altitude, onValueChange = { altitude = it.filter { c -> c.isDigit() } }, suffix = "msnm")
            LabeledTextField(label = "ÁREA CULTIVADA", value = area, onValueChange = { area = it.filter { c -> c.isDigit() } }, suffix = "hectáreas")
            LabeledTextField(label = "VARIEDADES PRINCIPALES", value = varieties, onValueChange = { varieties = it })
            LabeledTextField(label = "TIPO DE SOMBRA", value = shadeType, onValueChange = { shadeType = it })

            // Proceso post-cosecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "PROCESO DE POST-COSECHA", color = BrandColors.TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                Text(text = "+ AGREGAR PASO", color = BrandColors.FarmerPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { onAction("Agregar paso") })
            }
            Column(verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)) {
                ProcessRow(emoji = "✋", title = "Recolección manual selectiva", description = "Solo frutos en su punto óptimo de madurez (sangre de toro).")
                ProcessRow(emoji = "🫧", title = "Fermentación anaeróbica", description = "48 horas en tanques sellados con control estricto de pH y temperatura.")
                ProcessRow(emoji = "☀️", title = "Secado en marquesina", description = "Secado lento sobre camas africanas bajo sombra controlada.")
                ProcessRow(emoji = "📦", title = "Estabilización", description = "Reposo de 30 días en pergamino antes de la trilla final.")
            }

            Spacer(modifier = Modifier.height(BrandSpacing.lg))
            Button(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandColors.FarmerPrimary, contentColor = Color.White)
            ) {
                Text(text = "Guardar cambios", fontWeight = FontWeight.SemiBold)
            }
            Spacer(modifier = Modifier.height(BrandSpacing.lg))
        }
    }
}

@Composable
private fun PhotoSlot(modifier: Modifier = Modifier, isPrincipal: Boolean = false, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .height(110.dp)
            .background(BrandColors.InputBackground, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "📷", fontSize = 32.sp)
        if (isPrincipal) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(6.dp)
                    .background(BrandColors.FarmerPrimary, RoundedCornerShape(50))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(text = "PRINCIPAL", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
            }
        }
    }
}

@Composable
private fun AddPhotoSlot(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .height(110.dp)
            .background(BrandColors.AuthBackground, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Outlined.PhotoCamera, contentDescription = null, tint = BrandColors.TextSecondary, modifier = Modifier.size(20.dp))
            Text(text = "Agregar más", color = BrandColors.TextSecondary, fontSize = 11.sp)
        }
    }
}

@Composable
private fun ProcessRow(emoji: String, title: String, description: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .padding(BrandSpacing.sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(36.dp).background(Color(0xFFE8F4EC), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) { Text(text = emoji, fontSize = 18.sp) }
        Column(modifier = Modifier.weight(1f).padding(horizontal = BrandSpacing.sm)) {
            Text(text = title, color = BrandColors.TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Text(text = description, color = BrandColors.TextSecondary, fontSize = 11.sp, lineHeight = 14.sp)
        }
    }
}
