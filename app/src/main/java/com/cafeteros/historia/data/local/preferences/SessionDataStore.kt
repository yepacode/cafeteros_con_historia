package com.cafeteros.historia.data.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Extensión de [Context] que crea/recupera un único [androidx.datastore.core.DataStore]
 * con nombre "session" para toda la app.
 *
 * Vive como propiedad de extensión por convención: así cualquier
 * `applicationContext.sessionDataStore` apunta a la misma instancia y
 * DataStore garantiza concurrencia segura.
 */
private val Context.sessionDataStore by preferencesDataStore(name = "session")

/** Identificador del usuario actualmente logueado, o ausente si no hay sesión. */
private val KEY_CURRENT_USER_ID = longPreferencesKey("current_user_id")

/** Valor centinela para "no hay usuario logueado" en el campo `Long`. */
private const val NO_USER_ID: Long = -1L

/**
 * Maneja la sesión persistente de la app: qué usuario está logueado entre
 * arranques.
 *
 * Se eligió DataStore (no Room) porque la sesión es un dato escalar simple
 * (un id) y DataStore expone un [Flow] reactivo, perfecto para que la UI se
 * actualice automáticamente cuando se cierra/abre sesión.
 *
 * @param context cualquier [Context]; internamente se usa el `applicationContext`.
 */
class SessionDataStore(context: Context) {

    private val dataStore = context.applicationContext.sessionDataStore

    /**
     * Flujo del id del usuario logueado. Emite `null` cuando no hay sesión
     * activa.
     */
    val currentUserIdFlow: Flow<Long?> = dataStore.data.map { prefs: Preferences ->
        prefs[KEY_CURRENT_USER_ID]?.takeIf { it != NO_USER_ID }
    }

    /** Persiste el id del usuario logueado al iniciar/cerrar sesión. */
    suspend fun setCurrentUserId(userId: Long) {
        dataStore.edit { prefs ->
            prefs[KEY_CURRENT_USER_ID] = userId
        }
    }

    /** Borra la sesión actual (logout). */
    suspend fun clear() {
        dataStore.edit { prefs ->
            prefs.remove(KEY_CURRENT_USER_ID)
        }
    }
}
