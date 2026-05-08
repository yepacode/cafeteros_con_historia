package com.cafeteros.historia.ui.features.farmer_registration.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing

/**
 * Header del paso 5 (post-registro): título centrado "EL CAFICULTOR" en
 * itálica con un menú kebab a la derecha.
 *
 * Es un header distinto al del wizard (no tiene flecha de regreso ni progreso)
 * porque el usuario ya terminó el registro: ahora está en una pantalla de
 * estado, no de captura.
 *
 * @param modifier modifier opcional aplicado al [Row] contenedor.
 * @param title texto del título centrado.
 * @param onMenuClick callback al tocar el menú kebab. Por ahora abre opciones
 *  como cerrar sesión o ver términos; se cablea cuando exista el menú.
 */
@Composable
fun VerificationStatusHeader(
    modifier: Modifier = Modifier,
    title: String = "EL CAFICULTOR",
    onMenuClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = BrandSpacing.sm, vertical = BrandSpacing.sm)
    ) {
        Text(
            text = title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp),
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                fontStyle = FontStyle.Italic,
                letterSpacing = 1.sp,
                color = BrandColors.TextPrimary
            ),
            textAlign = TextAlign.Center
        )
        IconButton(
            onClick = onMenuClick,
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(
                imageVector = Icons.Outlined.MoreVert,
                contentDescription = "Más opciones",
                tint = BrandColors.TextPrimary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(widthDp = 360)
@Composable
private fun VerificationStatusHeaderPreview() {
    VerificationStatusHeader()
}
