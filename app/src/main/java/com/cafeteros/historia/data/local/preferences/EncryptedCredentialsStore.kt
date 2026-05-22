package com.cafeteros.historia.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
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
 *    valores anteriores son ilegibles. En ese caso esta clase los
 *    descarta automáticamente (ver [openOrReset]).
 *
 * En un proyecto en producción esto se complementaría con: refresh tokens
 * en lugar de la contraseña, atado obligatorio a [androidx.biometric.BiometricPrompt]
 * con `setUserAuthenticationParameters`, etc. Para el proyecto académico,
 * este nivel de seguridad cumple los criterios sin sobrecomplicar el código.
 */
class EncryptedCredentialsStore(context: Context) {

    private val appContext = context.applicationContext

    private val prefs: SharedPreferences by lazy { openOrReset() }

    /**
     * Abre el archivo cifrado. Si Tink lanza un error de descifrado
     * (típico tras desinstalar/reinstalar la app — la master key del
     * Keystore se regenera pero el archivo conserva datos del key viejo),
     * borramos el archivo, eliminamos la entrada del keyset roto en
     * SharedPreferences nativo y reintentamos. Las credenciales se
     * pierden silenciosamente; el usuario simplemente volverá a iniciar
     * con contraseña la próxima vez.
     */
    private fun openOrReset(): SharedPreferences {
        return runCatching { open() }
            .getOrElse { error ->
                Log.w(
                    TAG,
                    "Falló la carga de credenciales cifradas; reseteando bóveda. Causa: ${error.message}"
                )
                // Borrar el SharedPreferences del archivo cifrado.
                appContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
                    .edit().clear().apply()
                // Borrar también el `__androidx_security_crypto_encrypted_prefs_key_keyset__`
                // del SharedPreferences nativo, que es donde Tink guarda
                // el keyset cifrado por la master key del Keystore.
                appContext.deleteSharedPreferences(FILE_NAME)
                open()
            }
    }

    /** Construye el [EncryptedSharedPreferences] propiamente dicho. */
    private fun open(): SharedPreferences {
        val masterKey = MasterKey.Builder(appContext)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        return EncryptedSharedPreferences.create(
            appContext,
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
        runCatching {
            prefs.edit()
                .putString(KEY_EMAIL, email)
                .putString(KEY_PASSWORD, password)
                .apply()
        }.onFailure { Log.w(TAG, "saveCredentials falló: ${it.message}") }
    }

    /**
     * Devuelve las credenciales guardadas, o `null` si la bóveda está vacía
     * o no se pudo descifrar. El llamador es responsable de pasarlas a
     * Firebase Auth.
     */
    fun getCredentials(): Credentials? {
        return runCatching {
            val email = prefs.getString(KEY_EMAIL, null) ?: return null
            val password = prefs.getString(KEY_PASSWORD, null) ?: return null
            Credentials(email, password)
        }.getOrElse {
            Log.w(TAG, "getCredentials falló: ${it.message}")
            null
        }
    }

    /** Indica si hay credenciales válidas guardadas (sin descifrarlas dos veces). */
    fun hasCredentials(): Boolean {
        return runCatching {
            prefs.contains(KEY_EMAIL) && prefs.contains(KEY_PASSWORD)
        }.getOrElse {
            Log.w(TAG, "hasCredentials falló: ${it.message}")
            false
        }
    }

    /** Borra la bóveda. Útil cuando se "olvida" el dispositivo. */
    fun clearCredentials() {
        runCatching { prefs.edit().clear().apply() }
            .onFailure { Log.w(TAG, "clearCredentials falló: ${it.message}") }
    }

    /** Tupla simple para devolver credenciales sin exponer SharedPreferences. */
    data class Credentials(val email: String, val password: String)

    private companion object {
        const val TAG = "EncryptedCredsStore"
        const val FILE_NAME = "cafeteros_secure_credentials"
        const val KEY_EMAIL = "cred_email"
        const val KEY_PASSWORD = "cred_password"
    }
}
