package com.cafeteros.historia.ui.features.farmer_registration.steps

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing

/**
 * Paso 6 (post-verificación) del flujo de registro: pantalla de bienvenida
 * que confirma que la finca quedó aprobada y propone los 3 siguientes pasos
 * para empezar a vender.
 *
 * En modo demo se llega vía botón "Continuar" desde [VerificationStatusStep].
 * Con backend real, llegará por push o al refrescar el estado del usuario.
 *
 * @param userFirstName primer nombre del caficultor (para personalizar el saludo).
 * @param farmName nombre comercial de la finca capturado en el paso 2.
 * @param regionLabel zona cafetera detectada (departamento del paso 1).
 * @param onGoToPanel callback del CTA principal "Ir a mi panel".
 * @param onExplore callback del enlace secundario "Explorar la app primero".
 * @param onLogout callback opcional de "Cerrar sesión". Solo se renderiza
 *  cuando es no-null — útil cuando esta pantalla la muestra `MainActivity`
 *  para un caficultor con sesión activa. En el wizard de registro queda
 *  null porque la sesión recién se acaba de crear.
 */
@Composable
fun WelcomeApprovedStep(
    modifier: Modifier = Modifier,
    userFirstName: String?,
    farmName: String?,
    regionLabel: String?,
    onGoToPanel: () -> Unit,
    onExplore: () -> Unit,
    onLogout: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = BrandSpacing.lg, vertical = BrandSpacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        SuccessCheckCircle()

        Text(
            text = "¡Bienvenido${userFirstName?.let { ", $it" } ?: ""}!",
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = BrandColors.TextPrimary
            ),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Tu finca ya está aprobada en Origen",
            style = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontSize = 14.sp,
                color = BrandColors.TextSecondary
            ),
            textAlign = TextAlign.Center
        )

        FarmCard(
            farmName = farmName?.takeIf { it.isNotBlank() } ?: "Mi finca",
            regionLabel = regionLabel?.takeIf { it.isNotBlank() }
        )

        Spacer(modifier = Modifier.height(BrandSpacing.sm))
        Text(
            text = "Comienza a vender en 3 pasos",
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = BrandColors.TextPrimary
            ),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )

        SellStepCard(
            stepNumber = 1,
            emoji = "📦",
            title = "Publica tu primer producto",
            description = "Crea la ficha de tu primer café y súbelo al marketplace.",
            ctaLabel = "Crear producto",
            ctaPrimary = true,
            onCta = onGoToPanel
        )
        SellStepCard(
            stepNumber = 2,
            emoji = "💳",
            title = "Conecta tu cuenta bancaria",
            description = "Para recibir los pagos de tus ventas de forma segura.",
            ctaLabel = "Conectar cuenta",
            ctaPrimary = false,
            onCta = onGoToPanel
        )
        SellStepCard(
            stepNumber = 3,
            emoji = "🌟",
            title = "Completa tu perfil al 100%",
            description = "Perfiles completos venden 3x más.",
            ctaLabel = "Ver qué falta",
            ctaPrimary = false,
            onCta = onGoToPanel
        )

        Spacer(modifier = Modifier.height(BrandSpacing.md))
        Button(
            onClick = onGoToPanel,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BrandColors.CoffeeBrown,
                contentColor = BrandColors.PrimaryButtonText
            )
        ) {
            Text(
                text = "Ir a mi panel",
                fontFamily = FontFamily.SansSerif,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        TextButton(onClick = onExplore) {
            Text(
                text = "Explorar la app primero",
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 14.sp,
                    color = BrandColors.TextSecondary
                )
            )
        }

        if (onLogout != null) {
            TextButton(onClick = onLogout) {
                Text(
                    text = "Cerrar sesión",
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 13.sp,
                        color = BrandColors.TextSecondary
                    )
                )
            }
        }
    }
}

/** Círculo verde con check blanco — coronación visual de "verificación exitosa". */
@Composable
private fun SuccessCheckCircle() {
    Box(
        modifier = Modifier
            .size(72.dp)
            .background(color = BrandColors.FarmerPrimary, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(36.dp)
        )
    }
}

/**
 * Tarjeta de "VERIFICACIÓN COMPLETA" con nombre de la finca, zona y badge
 * dorado "CAFICULTOR VERIFICADO".
 */
@Composable
private fun FarmCard(farmName: String, regionLabel: String?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = BrandColors.CardBackground,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(BrandSpacing.md),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = BrandColors.FarmerPrimary,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = "VERIFICACIÓN COMPLETA",
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    color = BrandColors.FarmerPrimary
                )
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = regionLabel?.let { "$farmName · $it" } ?: farmName,
                modifier = Modifier.weight(1f),
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandColors.TextPrimary
                )
            )
            Box(
                modifier = Modifier
                    .background(
                        color = BrandColors.InfoBannerAction,
                        shape = RoundedCornerShape(50)
                    )
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "CAFICULTOR VERIFICADO",
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                        color = Color.White
                    )
                )
            }
        }
    }
}

/**
 * Card numerado de "Comienza a vender en 3 pasos". Botón verde primario o
 * outline secundario según [ctaPrimary].
 */
@Composable
private fun SellStepCard(
    stepNumber: Int,
    emoji: String,
    title: String,
    description: String,
    ctaLabel: String,
    ctaPrimary: Boolean,
    onCta: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = BrandColors.CardBackground,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(BrandSpacing.md),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(BrandColors.IndicatorInactive, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stepNumber.toString(),
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandColors.TextPrimary
                    )
                )
            }
            Text(
                text = "$emoji  $title",
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BrandColors.TextPrimary
                )
            )
        }
        Text(
            text = description,
            style = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontSize = 13.sp,
                color = BrandColors.TextSecondary
            )
        )
        if (ctaPrimary) {
            Button(
                onClick = onCta,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandColors.FarmerPrimary,
                    contentColor = BrandColors.PrimaryButtonText
                )
            ) {
                Text(text = ctaLabel, fontWeight = FontWeight.SemiBold)
            }
        } else {
            OutlinedButton(
                onClick = onCta,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = ctaLabel,
                    color = BrandColors.TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Preview(name = "WelcomeApproved", widthDp = 360, heightDp = 800)
@Composable
private fun WelcomeApprovedStepPreview() {
    WelcomeApprovedStep(
        userFirstName = "Don Alberto",
        farmName = "Finca La Esperanza",
        regionLabel = "Huila",
        onGoToPanel = {},
        onExplore = {}
    )
}
