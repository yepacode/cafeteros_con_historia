package com.cafeteros.historia

import android.app.Application
import com.cafeteros.historia.data.local.database.AppDatabase
import com.cafeteros.historia.data.local.preferences.SessionDataStore
import com.cafeteros.historia.data.repository.UserRepository

/**
 * Application class de Cafeteros.
 *
 * Su única responsabilidad es construir las dependencias singleton de la
 * app (base de datos, DataStore de sesión y repositorios) la primera vez
 * que se necesitan, y exponerlas vía propiedades `lazy`.
 *
 * Sin librería de inyección de dependencias (Hilt, Koin, etc.), este es el
 * patrón "service locator" — más que suficiente para el alcance actual y
 * sin agregar dependencias.
 *
 * Cualquier Activity puede acceder al repositorio así:
 * ```
 * val repo = (application as CafeterosApplication).userRepository
 * ```
 */
class CafeterosApplication : Application() {

    private val database: AppDatabase by lazy {
        AppDatabase.build(this)
    }

    private val sessionDataStore: SessionDataStore by lazy {
        SessionDataStore(this)
    }

    /** Acceso al repositorio único de usuarios + sesión. */
    val userRepository: UserRepository by lazy {
        UserRepository(
            userDao = database.userDao(),
            roleDao = database.roleDao(),
            sessionDataStore = sessionDataStore
        )
    }
}
