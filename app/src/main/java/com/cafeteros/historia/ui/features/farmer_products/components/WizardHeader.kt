package com.cafeteros.historia.ui.features.farmer_products.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.features.farmer_products.model.ProductCreationStep
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing

/**
 * Header del wizard de productos: flecha atrás + título + contador
 * "X de N" + barra de progreso segmentada.
 *
 * Hermano de [com.cafeteros.historia.ui.features.farmer_registration.components.RegistrationStepHeader]
 * pero con título configurable (porque aquí cambia entre "Nuevo producto"
 * y "Editar producto") y contador basado en [ProductCreationStep].
 */
@Composable
fun WizardHeader(
    modifier: Modifier = Modifier,
    title: String,
    currentStep: ProductCreationStep,
    onBack: () -> Unit
) {
    Column(modifier = modifier.fillMaxWidth().background(BrandColors.AuthBackground)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = BrandSpacing.sm),
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
                text = title,
                modifier = Modifier.weight(1f),
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    fontStyle = FontStyle.Italic,
                    color = BrandColors.TextPrimary
                ),
                textAlign = TextAlign.Center
            )
            Text(
                text = "${currentStep.number} de ${ProductCreationStep.TOTAL}",
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.sp,
                    color = BrandColors.TextSecondary
                ),
                modifier = Modifier.padding(end = BrandSpacing.md)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = BrandSpacing.lg)
                .padding(top = BrandSpacing.xs, bottom = BrandSpacing.sm),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            repeat(ProductCreationStep.TOTAL) { index ->
                val active = index < currentStep.number
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(3.dp)
                        .background(
                            color = if (active) BrandColors.FarmerPrimary
                            else BrandColors.IndicatorInactive,
                            shape = RoundedCornerShape(2.dp)
                        )
                )
            }
        }
    }
}
