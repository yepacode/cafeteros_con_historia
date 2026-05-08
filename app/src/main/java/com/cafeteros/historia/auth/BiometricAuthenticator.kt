package com.cafeteros.historia.auth

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import androidx.core.content.ContextCompat

/**
 * Resultado de un intento de autenticación biométrica.
 *
 * Se modela como sealed para que la UI maneje cada caso explícitamente sin
 * depender de los códigos numéricos crudos de `BiometricPrompt.ERROR_*`.
 */
sealed interface BiometricResult {
    /** El usuario se autenticó correctamente. */
    object Success : BiometricResult

    /** El usuario canceló el diálogo (botón "Cancelar" o tap fuera). */
    object Cancelled : BiometricResult

    /** El dispositivo no soporta biometría o no hay huellas registradas. */
    data class Unavailable(val reason: String) : BiometricResult

    /** Error genérico devuelto por el sistema. */
    data class Error(val code: Int, val message: String) : BiometricResult
}


class BiometricAuthenticator(private val activity: FragmentActivity) {

    private val executor = ContextCompat.getMainExecutor(activity)

    /**
     * Indica si el dispositivo puede realizar autenticación biométrica
     * en este momento (hardware presente + al menos una huella enrolada).
     */
    fun canAuthenticate(): BiometricResult {
        val manager = BiometricManager.from(activity)
        val authenticators = BIOMETRIC_STRONG or BIOMETRIC_WEAK
        return when (manager.canAuthenticate(authenticators)) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                BiometricResult.Success
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                BiometricResult.Unavailable("Este dispositivo no tiene sensor biométrico")
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                BiometricResult.Unavailable("El sensor biométrico no está disponible ahora")
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                BiometricResult.Unavailable("No tienes huellas registradas en el sistema")
            else ->
                BiometricResult.Unavailable("Biometría no disponible")
        }
    }

    /**
     * Lanza el [BiometricPrompt]. El [onResult] recibe el desenlace una sola vez.
     */
    fun authenticate(onResult: (BiometricResult) -> Unit) {
        val availability = canAuthenticate()
        if (availability is BiometricResult.Unavailable) {
            onResult(availability)
            return
        }

        val prompt = BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    onResult(BiometricResult.Success)
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    val cancelled = errorCode == BiometricPrompt.ERROR_USER_CANCELED ||
                            errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON ||
                            errorCode == BiometricPrompt.ERROR_CANCELED
                    onResult(
                        if (cancelled) BiometricResult.Cancelled
                        else BiometricResult.Error(errorCode, errString.toString())
                    )
                }

                override fun onAuthenticationFailed() {
                    // Fallo "suave" (huella no reconocida): el prompt sigue abierto y
                    // permite reintentar, así que no notificamos resultado aquí.
                }
            }
        )

        val info = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Iniciar sesión en Origen")
            .setSubtitle("Autenticación con huella dactilar")
            .setNegativeButtonText("Cancelar")
            .setAllowedAuthenticators(BIOMETRIC_STRONG or BIOMETRIC_WEAK)
            .build()

        prompt.authenticate(info)
    }
}

/**
 * Conveniencia para llamar [BiometricManager.from] sin importar el [Context]
 * de forma explícita en el resto de la app.
 */
fun Context.hasBiometricHardware(): Boolean {
    val authenticators = BIOMETRIC_STRONG or BIOMETRIC_WEAK
    return BiometricManager.from(this).canAuthenticate(authenticators) !=
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE
}
