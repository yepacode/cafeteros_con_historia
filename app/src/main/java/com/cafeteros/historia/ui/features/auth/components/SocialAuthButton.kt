package com.cafeteros.historia.ui.features.auth.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Fingerprint
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.R
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Botón blanco con borde sutil para login social. Reutilizado para Google y
 * Huella desde [com.cafeteros.historia.ui.features.auth.LoginScreen].
 *
 * El icono se acepta de dos formas alternativas:
 *  - [iconRes] para vectores multicolor (ej. logo de Google con sus 4 colores).
 *  - [iconVector] para iconos Material monocromos (ej. huella).
 *
 * Solo uno de los dos debe pasarse. Si llega [iconRes], gana.
 *
 * @param modifier modifier opcional.
 * @param onClick callback al pulsar.
 * @param label texto del botón ("Google", "Huella").
 * @param iconRes drawable multicolor (typ. logo de marca externa).
 * @param iconVector ImageVector de Material para iconos monocromos.
 * @param iconTint tint cuando se usa [iconVector]. Ignorado para [iconRes].
 */
@Composable
fun SocialAuthButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    label: String,
    iconRes: Int? = null,
    iconVector: ImageVector? = null,
    iconTint: Color = BrandColors.TextPrimary
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, BrandColors.SocialButtonBorder),
        colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
            containerColor = BrandColors.CardBackground,
            contentColor = BrandColors.TextPrimary
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            when {
                iconRes != null -> Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(20.dp)
                )
                iconVector != null -> Icon(
                    imageVector = iconVector,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                style = BrandTypography.SocialButtonLabel
            )
        }
    }
}

@Preview(name = "Social – Google", showBackground = true, widthDp = 200)
@Composable
private fun SocialAuthButtonGooglePreview() {
    CafeterosTheme {
        SocialAuthButton(
            onClick = {},
            label = "Google",
            iconRes = R.drawable.ic_google_g
        )
    }
}

@Preview(name = "Social – Huella", showBackground = true, widthDp = 200)
@Composable
private fun SocialAuthButtonFingerprintPreview() {
    CafeterosTheme {
        SocialAuthButton(
            onClick = {},
            label = "Huella",
            iconVector = Icons.Outlined.Fingerprint
        )
    }
}
