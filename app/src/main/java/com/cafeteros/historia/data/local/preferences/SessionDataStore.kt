package com.cafeteros.historia.data.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Único [androidx.datastore.core.DataStore] llamado "session" usado por la app.
 * Se expone como propiedad de extensión para garantizar que sea singleton.
 */
private val Context.sessionDataStore by preferencesDataStore(name = "session")

/**
 * Banderas locales de la sesión que complementan a Firebase Auth.
 *
 * **Firebase Auth ya persiste los tokens de la sesión por nosotros**, así que
 * ya no es necesario guardar `current_user_id`. Lo que sí guardamos aquí son
 * preferencias del dispositivo:
 *
 *  - [biometricEnabledFlow]: si el usuario activó el login con huella.
 *  - [lastEmailFlow]:        último correo usado, para autocompletar el
 *                            campo "Email" en la próxima apertura.
 *
 * Estas son preferencias *locales al dispositivo* — si el usuario se loguea
 * en otro celular, la app le pedirá email/clave aunque ya tenga sesión
 * activa en Firebase desde otro equipo.
 */
class SessionDataStore(context: Context) {

    private val dataStore = context.applicationContext.sessionDataStore

    /** `true` si el usuario aceptó usar biometría para reabrir la sesión. */
    val biometricEnabledFlow: Flow<Boolean> = dataStore.data.map { prefs: Preferences ->
        prefs[KEY_BIOMETRIC_ENABLED] ?: false
    }

    /** Último email usado para login; sirve para autocompletar. */
    val lastEmailFlow: Flow<String?> = dataStore.data.map { prefs: Preferences ->
        prefs[KEY_LAST_EMAIL]
    }

    /** Snapshot puntual del flag biométrico. */
    suspend fun isBiometricEnabled(): Boolean = biometricEnabledFlow.first()

    /** Snapshot puntual del último email. */
    suspend fun lastEmail(): String? = lastEmailFlow.first()

    /** Activa/desactiva el login biométrico. */
    suspend fun setBiometricEnabled(enabled: Boolean) {
        dataStore.edit { prefs -> prefs[KEY_BIOMETRIC_ENABLED] = enabled }
    }

    /** Guarda el último email usado al iniciar sesión correctamente. */
    suspend fun setLastEmail(email: String) {
        dataStore.edit { prefs -> prefs[KEY_LAST_EMAIL] = email }
    }

    /**
     * Limpia las banderas de sesión tras un logout: desactiva la biometría
     * (ya no hay sesión Firebase que desbloquear) pero **mantiene** el
     * último email para que el próximo login lo autocomplete. El email no
     * es información sensible y mejora la UX.
     */
    suspend fun clear() {
        dataStore.edit { prefs ->
            prefs.remove(KEY_BIOMETRIC_ENABLED)
            // Nota: NO removemos KEY_LAST_EMAIL a propósito.
        }
    }

    /** Limpia TODO incluyendo el último email. Solo para "olvidar dispositivo". */
    suspend fun clearAll() {
        dataStore.edit { prefs ->
            prefs.remove(KEY_BIOMETRIC_ENABLED)
            prefs.remove(KEY_LAST_EMAIL)
        }
    }

    private companion object {
        val KEY_BIOMETRIC_ENABLED = booleanPreferencesKey("biometric_enabled")
        val KEY_LAST_EMAIL = stringPreferencesKey("last_email")
    }
}
