package com.cafeteros.historia.ui.features.farmer_profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cafeteros.historia.data.model.SUGGESTED_CERTIFICATIONS
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity contenedora de [CertificationsScreen]. Lee/escribe las
 * certificaciones de la finca a través de [CertificationsViewModel] (que
 * a su vez delega en [com.cafeteros.historia.data.repository.FarmRepository]).
 */
class CertificationsActivity : ComponentActivity() {

    private val viewModel: CertificationsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                val active by viewModel.activeCertifications.collectAsStateWithLifecycle()
                val isSaving by viewModel.isSaving.collectAsStateWithLifecycle()
                val toast by viewModel.toast.collectAsStateWithLifecycle()

                LaunchedEffect(toast) {
                    toast?.let {
                        Toast.makeText(this@CertificationsActivity, it, Toast.LENGTH_SHORT).show()
                        viewModel.consumeToast()
                    }
                }

                CertificationsScreen(
                    activeCerts = active,
                    suggestions = SUGGESTED_CERTIFICATIONS.filter { it !in active },
                    isSaving = isSaving,
                    onBack = ::finish,
                    onAdd = viewModel::add,
                    onRemove = viewModel::remove
                )
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, CertificationsActivity::class.java))
        }
    }
}

/**
 * Pantalla de gestión de certificaciones de la finca.
 *
 * Stateless: recibe la lista actual + sugerencias y delega cambios. La
 * persistencia la hace el ViewModel contenedor llamando al
 * [com.cafeteros.historia.data.repository.FarmRepository].
 *
 * @param activeCerts certificaciones que el caficultor ya declaró.
 * @param suggestions catálogo predefinido filtrado para excluir las que ya
 *  están activas.
 * @param isSaving deshabilita los tap mientras se persiste en Firestore
 *  para evitar dobles guardados.
 * @param onBack cerrar la activity.
 * @param onAdd agregar una certificación sugerida (o un texto libre).
 * @param onRemove quitar una certificación activa.
 */
@Composable
fun CertificationsScreen(
    activeCerts: List<String>,
    suggestions: List<String>,
    isSaving: Boolean,
    onBack: () -> Unit,
    onAdd: (String) -> Unit,
    onRemove: (String) -> Unit,
    modifier: Modifier = Modifier
) {
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
            Spacer(modifier = Modifier.height(BrandSpacing.sm))

            // Sección activas
            SectionLabel("CERTIFICACIONES ACTIVAS (${activeCerts.size})")
            if (activeCerts.isEmpty()) {
                EmptyState()
            } else {
                activeCerts.forEach { cert ->
                    ActiveCertRow(name = cert, isSaving = isSaving, onRemove = { onRemove(cert) })
                }
            }

            // Sección sugerencias
            if (suggestions.isNotEmpty()) {
                Spacer(modifier = Modifier.height(BrandSpacing.sm))
                SectionLabel("DISPONIBLES PARA AGREGAR")
                suggestions.forEach { cert ->
                    SuggestionRow(name = cert, isSaving = isSaving, onAdd = { onAdd(cert) })
                }
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
            text = "Certificaciones",
            modifier = Modifier.weight(1f),
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = BrandColors.TextPrimary
            )
        )
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        color = BrandColors.TextSecondary,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.8.sp,
        modifier = Modifier.padding(vertical = BrandSpacing.xs)
    )
}

@Composable
private fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .padding(BrandSpacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Icon(
            imageVector = Icons.Outlined.WorkspacePremium,
            contentDescription = null,
            tint = BrandColors.FarmerPrimary,
            modifier = Modifier.size(36.dp)
        )
        Text(
            text = "Aún no declaras certificaciones",
            color = BrandColors.TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "Agrega las que tu finca tenga vigentes para destacar ante compradores.",
            color = BrandColors.TextSecondary,
            fontSize = 11.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
private fun ActiveCertRow(name: String, isSaving: Boolean, onRemove: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .padding(BrandSpacing.md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(Color(0xFFE8F4EC), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.WorkspacePremium,
                contentDescription = null,
                tint = BrandColors.FarmerPrimary,
                modifier = Modifier.size(18.dp)
            )
        }
        Text(
            text = name,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = BrandSpacing.sm),
            color = BrandColors.TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
        IconButton(
            onClick = onRemove,
            enabled = !isSaving
        ) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = "Quitar",
                tint = BrandColors.TextSecondary
            )
        }
    }
}

@Composable
private fun SuggestionRow(name: String, isSaving: Boolean, onAdd: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .clickable(enabled = !isSaving, onClick = onAdd)
            .padding(BrandSpacing.md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            modifier = Modifier.weight(1f),
            color = BrandColors.TextPrimary,
            fontSize = 14.sp
        )
        Icon(
            imageVector = Icons.Outlined.Add,
            contentDescription = "Agregar",
            tint = BrandColors.FarmerPrimary
        )
    }
}
