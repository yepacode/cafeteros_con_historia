package com.cafeteros.historia.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/**
 * Bóveda local de credenciales (email + contraseña) cifrada con AES-256.
 *
 * **Por qué existe.** Firebase Auth no permite "rehidratar" una sesión a
 * partir de un identificador biométrico — la única forma de iniciar sesión
 * sin escribir contraseña es invocando `signInWithEmailAndPassword` con las
 * credenciales reales. Para que el botón "Iniciar con huella" del Login
 * funcione realmente como login (no como simple desbloqueo de una sesión ya
 * activa), guardamos esas credenciales tras un login exitoso con contraseña
 * y las recuperamos cuando el usuario autentica con su huella.
 *
 * **Seguridad.** Se utiliza [EncryptedSharedPreferences] del paquete
 * `androidx.security`, que:
 *  - Cifra valores con AES-256-GCM y claves con AES-256-SIV.
 *  - La master key se respalda en el Android Keystore (TEE / Trusted
 *    Execution Environment del SoC), no en disco plano.
 *  - Si el usuario reinstala la app, la master key se regenera y los
 *    valores anteriores son ilegibles.
 *
 * En un proyecto en producción esto se complementaría con: refresh tokens
 * en lugar de la contraseña, atado obligatorio a [androidx.biometric.BiometricPrompt]
 * con `setUserAuthenticationParameters`, etc. Para el proyecto académico,
 * este nivel de seguridad cumple los criterios sin sobrecomplicar el código.
 */
class EncryptedCredentialsStore(context: Context) {

    private val prefs: SharedPreferences by lazy {
        val masterKey = MasterKey.Builder(context.applicationContext)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        EncryptedSharedPreferences.create(
            context.applicationContext,
            FILE_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    /**
     * Guarda las credenciales del último login exitoso. Sobrescribe lo que
     * hubiera antes — solo conservamos la cuenta más reciente.
     */
    fun saveCredentials(email: String, password: String) {
        prefs.edit()
            .putString(KEY_EMAIL, email)
            .putString(KEY_PASSWORD, password)
            .apply()
    }

    /**
     * Devuelve las credenciales guardadas, o `null` si la bóveda está vacía.
     * El llamador es responsable de pasarlas a Firebase Auth.
     */
    fun getCredentials(): Credentials? {
        val email = prefs.getString(KEY_EMAIL, null) ?: return null
        val password = prefs.getString(KEY_PASSWORD, null) ?: return null
        return Credentials(email, password)
    }

    /** Indica si hay credenciales válidas guardadas (sin descifrarlas dos veces). */
    fun hasCredentials(): Boolean =
        prefs.contains(KEY_EMAIL) && prefs.contains(KEY_PASSWORD)

    /** Borra la bóveda. Útil cuando se "olvida" el dispositivo. */
    fun clearCredentials() {
        prefs.edit().clear().apply()
    }

    /** Tupla simple para devolver credenciales sin exponer SharedPreferences. */
    data class Credentials(val email: String, val password: String)

    private companion object {
        const val FILE_NAME = "cafeteros_secure_credentials"
        const val KEY_EMAIL = "cred_email"
        const val KEY_PASSWORD = "cred_password"
    }
}
