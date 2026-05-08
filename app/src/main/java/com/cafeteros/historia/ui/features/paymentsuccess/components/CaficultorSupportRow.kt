package com.cafeteros.historia.ui.features.paymentsuccess.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.paymentsuccess.model.SupportedCaficultor
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Fila de un caficultor apoyado en la sección "Con esta compra apoyaste a:".
 *
 * Layout: avatar circular con borde dorado a la izquierda, nombre de la
 * finca y propietario en el centro, enlace verde "Ver perfil" a la derecha.
 *
 * @param modifier modifier opcional.
 * @param caficultor caficultor a renderizar.
 * @param onViewProfile callback del enlace "Ver perfil".
 */
@Composable
fun CaficultorSupportRow(
    modifier: Modifier = Modifier,
    caficultor: SupportedCaficultor,
    onViewProfile: () -> Unit = {}
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        AvatarPlaceholder(initials = caficultor.avatarInitials)
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(text = caficultor.fincaName, style = BrandTypography.SupportedCaficultorName)
            Text(text = caficultor.ownerName, style = BrandTypography.SupportedCaficultorOwner)
        }
        Text(
            text = "Ver perfil",
            style = BrandTypography.SupportedCaficultorViewProfile,
            modifier = Modifier.clickable(onClick = onViewProfile)
        )
    }
}

@Composable
private fun AvatarPlaceholder(modifier: Modifier = Modifier, initials: String) {
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(BrandColors.SupportedCaficultorAvatarFallbackBackground)
            .border(
                width = 2.dp,
                color = BrandColors.SupportedCaficultorAvatarBorder,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(text = initials, style = BrandTypography.SupportedCaficultorAvatarLabel)
    }
}

@Preview(name = "CaficultorSupportRow", showBackground = true, widthDp = 360)
@Composable
private fun CaficultorSupportRowPreview() {
    CafeterosTheme {
        CaficultorSupportRow(
            modifier = Modifier.padding(BrandSpacing.lg),
            caficultor = SupportedCaficultor(
                id = "finca-la-esperanza",
                fincaName = "Finca La Esperanza",
                ownerName = "Alberto Rodríguez",
                avatarInitials = "AR"
            )
        )
    }
}
