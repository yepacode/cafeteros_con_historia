package com.cafeteros.historia.ui.features.filters

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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.data.model.ProductCategory
import com.cafeteros.historia.ui.features.searchresults.SearchResultsActivity
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity de filtros del comprador. Replaza la antigua mock con UI rica
 * por una versión simple pero funcional:
 *
 *  - Rango de precio (min / max en COP).
 *  - Categoría del producto (chips multi-select).
 *  - Región / departamento (chips multi-select, extraídos del FarmProfile
 *    a través de la lista de productos en SearchResults).
 *  - Switch "Solo orgánico".
 *
 * El resultado se serializa como extras del Intent que abre
 * [SearchResultsActivity], que es quien observa Firestore y aplica los
 * filtros en cliente.
 */
class OriginFiltersActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                OriginFiltersScreen(
                    onClose = ::finish,
                    onApply = { state: FilterFormState ->
                        SearchResultsActivity.start(
                            context = this,
                            priceMin = state.priceMinCop,
                            priceMax = state.priceMaxCop,
                            categories = state.categories.map { it.name },
                            regions = state.regions,
                            onlyOrganic = state.onlyOrganic
                        )
                        finish()
                    }
                )
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, OriginFiltersActivity::class.java))
        }
    }
}

/** Estado del form de filtros. */
internal data class FilterFormState(
    val priceMinCop: Int = 0,
    val priceMaxCop: Int = 0,
    val categories: Set<ProductCategory> = emptySet(),
    val regions: Set<String> = emptySet(),
    val onlyOrganic: Boolean = false
)

private val SUGGESTED_REGIONS: List<String> = listOf(
    "Huila", "Antioquia", "Nariño", "Caldas", "Quindío", "Risaralda", "Cauca", "Tolima"
)

@Composable
private fun OriginFiltersScreen(
    onClose: () -> Unit,
    onApply: (FilterFormState) -> Unit
) {
    var priceMin by remember { mutableStateOf("") }
    var priceMax by remember { mutableStateOf("") }
    var selectedCategories by remember { mutableStateOf<Set<ProductCategory>>(emptySet()) }
    var selectedRegions by remember { mutableStateOf<Set<String>>(emptySet()) }
    var onlyOrganic by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(BrandColors.AuthBackground)
        .systemBarsPadding()) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = BrandSpacing.sm, vertical = BrandSpacing.xs),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onClose) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Cerrar",
                    tint = BrandColors.TextPrimary
                )
            }
            Text(
                text = "Filtros",
                modifier = Modifier.weight(1f),
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandColors.TextPrimary
                )
            )
            Text(
                text = "LIMPIAR",
                modifier = Modifier
                    .clickable {
                        priceMin = ""; priceMax = ""
                        selectedCategories = emptySet()
                        selectedRegions = emptySet()
                        onlyOrganic = false
                    }
                    .padding(BrandSpacing.sm),
                color = BrandColors.FarmerPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = BrandSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(BrandSpacing.lg)
        ) {
            Spacer(modifier = Modifier.height(BrandSpacing.sm))

            // ── Rango de precio ─────────────────────────────────────
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SectionTitle("RANGO DE PRECIO (COP)")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
                ) {
                    PriceInput(
                        modifier = Modifier.weight(1f),
                        label = "Mínimo",
                        value = priceMin,
                        onChange = { priceMin = it.filter { c -> c.isDigit() } }
                    )
                    PriceInput(
                        modifier = Modifier.weight(1f),
                        label = "Máximo",
                        value = priceMax,
                        onChange = { priceMax = it.filter { c -> c.isDigit() } }
                    )
                }
            }

            // ── Categoría ───────────────────────────────────────────
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SectionTitle("CATEGORÍA")
                ChipsFlow(
                    options = ProductCategory.entries.map { it.label },
                    selectedLabels = selectedCategories.map { it.label }.toSet(),
                    onToggle = { label ->
                        val cat = ProductCategory.entries.first { it.label == label }
                        selectedCategories = if (cat in selectedCategories)
                            selectedCategories - cat else selectedCategories + cat
                    }
                )
            }

            // ── Región ──────────────────────────────────────────────
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SectionTitle("REGIÓN / DEPARTAMENTO")
                ChipsFlow(
                    options = SUGGESTED_REGIONS,
                    selectedLabels = selectedRegions,
                    onToggle = { region ->
                        selectedRegions = if (region in selectedRegions)
                            selectedRegions - region else selectedRegions + region
                    }
                )
            }

            // ── Solo orgánico ───────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
                    .padding(BrandSpacing.md),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Solo cafés orgánicos",
                        color = BrandColors.TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Productos con certificación orgánica declarada por el caficultor.",
                        color = BrandColors.TextSecondary,
                        fontSize = 11.sp
                    )
                }
                Switch(
                    checked = onlyOrganic,
                    onCheckedChange = { onlyOrganic = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = BrandColors.FarmerPrimary,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = BrandColors.IndicatorInactive
                    )
                )
            }

            Spacer(modifier = Modifier.height(BrandSpacing.lg))
        }

        // Bottom: aplicar / cancelar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(BrandColors.CardBackground)
                .padding(BrandSpacing.md),
            horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
        ) {
            OutlinedButton(
                onClick = onClose,
                modifier = Modifier.weight(1f).height(48.dp)
            ) {
                Text(text = "Cancelar", color = BrandColors.TextPrimary)
            }
            Button(
                onClick = {
                    onApply(
                        FilterFormState(
                            priceMinCop = priceMin.toIntOrNull() ?: 0,
                            priceMaxCop = priceMax.toIntOrNull() ?: 0,
                            categories = selectedCategories,
                            regions = selectedRegions,
                            onlyOrganic = onlyOrganic
                        )
                    )
                },
                modifier = Modifier.weight(1.4f).height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandColors.CoffeeBrown,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Aplicar filtros", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        color = BrandColors.TextSecondary,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.8.sp
    )
}

@Composable
private fun PriceInput(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onChange: (String) -> Unit
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label,
            color = BrandColors.TextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(BrandColors.InputBackground, RoundedCornerShape(12.dp))
                .padding(horizontal = BrandSpacing.md, vertical = 12.dp)
        ) {
            if (value.isBlank()) {
                Text(
                    text = "$ —",
                    color = BrandColors.TextSecondary,
                    fontSize = 13.sp,
                    fontStyle = FontStyle.Italic
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onChange,
                singleLine = true,
                textStyle = TextStyle(
                    color = BrandColors.TextPrimary,
                    fontSize = 13.sp
                )
            )
        }
    }
}

/**
 * Fila de chips de selección múltiple que envuelve cuando se queda sin
 * espacio. Compose 1.x no tiene FlowRow estable, así que usamos `Column`
 * con `Row`s manuales — para los <10 chips esperados, basta.
 */
@Composable
private fun ChipsFlow(
    options: List<String>,
    selectedLabels: Set<String>,
    onToggle: (String) -> Unit
) {
    // Particionamos en filas de 3 chips para que el wrap sea predecible.
    options.chunked(3).forEach { rowOptions ->
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
        ) {
            rowOptions.forEach { label ->
                FilterChip(
                    label = label,
                    selected = label in selectedLabels,
                    onClick = { onToggle(label) },
                    modifier = Modifier.weight(1f)
                )
            }
            // Si la fila tiene menos de 3, rellenar para mantener el ancho.
            repeat(3 - rowOptions.size) {
                Box(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun FilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = if (selected) BrandColors.CoffeeBrown else BrandColors.CardBackground,
                shape = RoundedCornerShape(50)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = BrandSpacing.sm, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (selected) BrandColors.CreamWhite else BrandColors.TextPrimary,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1
        )
    }
}
