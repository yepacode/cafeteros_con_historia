

package com.cafeteros.historia.ui.features.debug

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.MainActivity
import com.cafeteros.historia.ui.features.auth.LoginActivity
import com.cafeteros.historia.ui.features.auth.RegisterActivity
import com.cafeteros.historia.ui.features.auth.components.UserType
import com.cafeteros.historia.ui.features.farmer_onboarding.FarmerOnboardingActivity
import com.cafeteros.historia.ui.features.farmer_registration.FarmerRegistrationActivity
import com.cafeteros.historia.ui.features.onboarding.OnboardingActivity
import com.cafeteros.historia.ui.features.password_recovery.PasswordRecoveryActivity
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Pantalla de **debug** con botones que abren cada Activity de la app.
 *
 * Útil mientras se desarrolla y se necesita revisar pantallas que no son
 * fácilmente alcanzables desde el flujo normal del usuario.
 *
 * Se abre haciendo **long-press en el logo del Splash** (gesto oculto). No
 * tiene entrada en el AndroidManifest como LAUNCHER ni se expone públicamente.
 *
 * Cuando la app esté lista para producción, se puede:
 *  - Mover esta clase a un source set de `debug/` (Gradle build variants).
 *  - O envolverla con un check `BuildConfig.DEBUG` para que solo abra en
 *    builds debug.
 */
class DebugNavigationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                DebugNavigationScreen(onClose = ::finish)
            }
        }
    }
}

@Composable
private fun DebugNavigationScreen(onClose: () -> Unit) {
    val context = LocalContext.current

    fun launch(activityClass: Class<*>, extras: (Intent.() -> Unit)? = null) {
        val intent = Intent(context, activityClass).apply { extras?.invoke(this) }
        context.startActivity(intent)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(BrandSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Text(
            text = "🛠 Debug navigation",
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = BrandColors.TextPrimary
            )
        )
        Text(
            text = "Atajos para saltar a cualquier pantalla mientras desarrollas. " +
                    "Esta pantalla solo es visible para el equipo y se elimina antes de publicar.",
            style = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                color = BrandColors.TextSecondary
            )
        )

        Spacer(modifier = Modifier.height(BrandSpacing.md))
        SectionTitle("Onboarding y autenticación")
        DebugButton("Onboarding general (comprador)") {
            launch(OnboardingActivity::class.java)
        }
        DebugButton("Login") { launch(LoginActivity::class.java) }
        DebugButton("Registrarse (Comprador)") {
            launch(RegisterActivity::class.java) {
                putExtra(RegisterActivity.EXTRA_INITIAL_USER_TYPE, UserType.COMPRADOR.name)
            }
        }
        DebugButton("Registrarse (Caficultor → callout)") {
            launch(RegisterActivity::class.java) {
                putExtra(RegisterActivity.EXTRA_INITIAL_USER_TYPE, UserType.CAFICULTOR.name)
            }
        }
        DebugButton("Recuperar contraseña") {
            launch(PasswordRecoveryActivity::class.java)
        }

        Spacer(modifier = Modifier.height(BrandSpacing.md))
        SectionTitle("Flujo del Caficultor")
        DebugButton("Onboarding caficultor (3 verdes)") {
            launch(FarmerOnboardingActivity::class.java)
        }
        DebugButton("Registro caficultor (5 pasos)") {
            launch(FarmerRegistrationActivity::class.java)
        }

        Spacer(modifier = Modifier.height(BrandSpacing.md))
        SectionTitle("Home")
        DebugButton("MainActivity (rutea por rol)") {
            launch(MainActivity::class.java)
        }

        Spacer(modifier = Modifier.height(BrandSpacing.lg))
        Button(
            onClick = onClose,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BrandColors.IndicatorInactive,
                contentColor = BrandColors.TextPrimary
            )
        ) {
            Text("Cerrar")
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.sp,
            color = BrandColors.TextSecondary
        )
    )
}

@Composable
private fun DebugButton(label: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = BrandColors.CoffeeBrown,
            contentColor = BrandColors.PrimaryButtonText
        )
    ) {
        Text(text = label)
    }
}
