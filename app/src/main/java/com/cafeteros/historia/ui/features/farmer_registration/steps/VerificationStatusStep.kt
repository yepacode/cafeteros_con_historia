package com.cafeteros.historia.ui.features.farmer_registration.steps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.Share
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.features.farmer_registration.components.BottomNavStub
import com.cafeteros.historia.ui.features.farmer_registration.components.HeroVerificationCard
import com.cafeteros.historia.ui.features.farmer_registration.components.MeantimeContentCard
import com.cafeteros.historia.ui.features.farmer_registration.components.ProgressTimelineCard
import com.cafeteros.historia.ui.features.farmer_registration.components.SupportFooter
import com.cafeteros.historia.ui.features.farmer_registration.components.VerificationStatusHeader
import com.cafeteros.historia.ui.features.farmer_registration.model.VerificationProgressItem
import com.cafeteros.historia.ui.features.farmer_registration.model.VerificationProgressStatus
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing

/**
 * Datos mock del timeline de verificación. En producción vendrá del backend
 * vía polling cada N segundos o vía push notifications.
 *
 * El status simulado avanza: 2 listos, 1 en curso, 2 pendientes.
 */
private val DEMO_PROGRESS_ITEMS: List<VerificationProgressItem> = listOf(
    VerificationProgressItem(
        title = "Registro recibido",
        subtitle = "Hoy, 10 abril 11:55",
        status = VerificationProgressStatus.DONE
    ),
    VerificationProgressItem(
        title = "Documentos revisados",
        subtitle = "Hoy, 14:35",
        status = VerificationProgressStatus.DONE
    ),
    VerificationProgressItem(
        title = "Verificación de identidad",
        subtitle = "EN CURSO",
        status = VerificationProgressStatus.IN_PROGRESS
    ),
    VerificationProgressItem(
        title = "Verificación de finca",
        subtitle = "Pendiente",
        status = VerificationProgressStatus.PENDING
    ),
    VerificationProgressItem(
        title = "Activación final",
        subtitle = "Pendiente",
        status = VerificationProgressStatus.PENDING
    )
)

/**
 * Paso 5 del registro: pantalla post-envío "Estamos revisando tu finca".
 *
 * No es un formulario — es una vista de estado. Tiene su propio shell
 * (header centrado + bottom nav stub) en lugar del header del wizard.
 *
 * @param modifier modifier opcional aplicado al [Column] raíz.
 * @param userFirstName nombre del caficultor para personalizar el saludo
 *  del hero. Viene del paso 1 del formulario.
 * @param onContactSupport callback de "Contactar soporte".
 * @param onLogout callback de "Cerrar sesión".
 * @param onAdvanceDemo callback opcional del botón "Continuar" (solo se
 *  muestra cuando es no-null). En modo demo lo usa el wizard de registro
 *  para saltar a la pantalla de bienvenida sin esperar al backend; cuando
 *  MainActivity reusa esta pantalla para usuarios pendientes de verificar,
 *  se omite y el botón no aparece.
 */
@Composable
fun VerificationStatusStep(
    modifier: Modifier = Modifier,
    userFirstName: String?,
    onContactSupport: () -> Unit,
    onLogout: () -> Unit,
    onAdvanceDemo: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding()
    ) {
        VerificationStatusHeader()

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = BrandSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
        ) {
            HeroVerificationCard(userFirstName = userFirstName)

            Spacer(modifier = Modifier.height(BrandSpacing.xs))
            SectionLabel(text = "Progreso de tu verificación")
            ProgressTimelineCard(items = DEMO_PROGRESS_ITEMS)

            Spacer(modifier = Modifier.height(BrandSpacing.sm))
            SectionLabel(text = "Mientras tanto…")
            MeantimeContentCard(
                icon = Icons.Outlined.MenuBook,
                iconBackground = BrandColors.FarmerPrimary,
                title = "Guía del caficultor exitoso",
                description = "Aprende a tomar mejores fotos de tus granos para vender más rápido.",
                actionLabel = "Leer guía →",
                onActionClick = { /* TODO: abrir guía */ }
            )
            MeantimeContentCard(
                icon = Icons.Outlined.Share,
                iconBackground = BrandColors.InfoBannerAction,
                title = "Comparte que estás en Origen",
                description = "Cuéntale a tu familia y amigos que estás aquí, pronto podrán comprarte café.",
                actionLabel = "Compartir →",
                onActionClick = { /* TODO: compartir */ }
            )
            MeantimeContentCard(
                icon = Icons.Outlined.PlayCircle,
                iconBackground = BrandColors.CoffeeBrown,
                title = "Tutorial en video",
                description = "Cómo gestionar tus productos y responder pedidos en la plataforma.",
                actionLabel = "Ver video →",
                onActionClick = { /* TODO: abrir video */ }
            )

            // ⚠️ El botón "Continuar" que saltaba a WelcomeApproved fue
            // removido a propósito: la aprobación ahora la hace un
            // administrador desde su panel revisando los documentos.
            // El caficultor debe esperar; cuando admin apruebe, el
            // observer del flow en MainActivity lo enrutará al panel.
            // El parámetro `onAdvanceDemo` se conserva en la firma para
            // no romper callers pero ya no se usa visualmente.
            @Suppress("UNUSED_EXPRESSION") onAdvanceDemo

            Spacer(modifier = Modifier.height(BrandSpacing.lg))
            SupportFooter(
                onContactSupport = onContactSupport,
                onLogout = onLogout
            )
            Spacer(modifier = Modifier.height(BrandSpacing.lg))
        }

        BottomNavStub()
    }
}

/** Etiqueta tipo título de sección, en serif itálica de marca. */
@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        style = TextStyle(
            fontFamily = FontFamily.Serif,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            fontStyle = FontStyle.Italic,
            color = BrandColors.TextPrimary
        )
    )
}
