package com.cafeteros.historia.ui.features.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cafeteros.historia.CafeterosApplication
import com.cafeteros.historia.data.local.preferences.SessionDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Estado UI de la pantalla de Configuración.
 *
 * @param biometricEnabled refleja el valor actual del flag persistido en
 *  [SessionDataStore]. La UI lo usa para dibujar el `Switch` en posición
 *  encendida o apagada.
 * @param biometricAvailable true si el dispositivo tiene sensor biométrico
 *  con al menos una huella enrolada. Cuando es false, el Switch debe ir
 *  deshabilitado y mostrarse [biometricUnavailableReason] como explicación.
 * @param biometricUnavailableReason texto humano-legible que explica por qué
 *  la huella no se puede usar (sin sensor, sensor caído temporalmente, sin
 *  huellas registradas en el sistema, etc.). Solo se muestra si
 *  [biometricAvailable] es false.
 */
data class SettingsUiState(
    val biometricEnabled: Boolean = false,
    val biometricAvailable: Boolean = true,
    val biometricUnavailableReason: String? = null
)

/**
 * ViewModel de la pantalla de Configuración.
 *
 * Centraliza la lectura/escritura de las preferencias del dispositivo que
 * el usuario controla manualmente. Hoy solo expone el toggle de biometría,
 * pero el archivo está preparado para crecer (tema, idioma, notificaciones).
 *
 * **Política del toggle de huella:**
 *  - El flag [SessionDataStore.biometricEnabledFlow] solo se pone en `true`
 *    *después* de que la Activity verifique al usuario con un
 *    `BiometricPrompt`. Esta verificación se hace en la Activity (no aquí)
 *    porque `BiometricPrompt` necesita un `FragmentActivity`.
 *  - La Activity llama [setBiometricEnabled] solo cuando ya autenticó al
 *    usuario; el ViewModel se limita a persistir el valor decidido.
 *  - Al apagar el toggle (true → false) la verificación no es necesaria:
 *    apagar la huella es una operación de degradación de seguridad
 *    autorizada implícitamente por el usuario al estar dentro de su sesión.
 */
class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    /** Snapshot interno de disponibilidad reportada por la Activity. */
    private data class Availability(val available: Boolean, val reason: String?)

    private val sessionPreferences: SessionDataStore =
        (application as CafeterosApplication).sessionPreferences

    /**
     * Disponibilidad de biometría calculada por la Activity (que tiene el
     * `BiometricManager`). Se inicializa optimistamente en `available=true`
     * para evitar que el Switch parpadee deshabilitado durante el primer
     * frame, hasta que la Activity reporte el valor real en
     * [updateBiometricAvailability] sobre `onCreate`.
     */
    private val _availability = MutableStateFlow(
        Availability(available = true, reason = null)
    )

    /**
     * Estado combinado de la pantalla. Une la flag persistida en DataStore
     * con la disponibilidad transitoria de biometría. Cualquier cambio en
     * cualquiera de las dos dispara una recomposición de la UI.
     */
    val uiState: StateFlow<SettingsUiState> = combine(
        sessionPreferences.biometricEnabledFlow,
        _availability
    ) { enabled, availability ->
        SettingsUiState(
            biometricEnabled = enabled,
            biometricAvailable = availability.available,
            biometricUnavailableReason = availability.reason
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SettingsUiState()
    )

    /**
     * Persiste el valor del toggle. La verificación con `BiometricPrompt`
     * debe haberse hecho *antes* de llamar esta función con `enabled=true`.
     */
    fun setBiometricEnabled(enabled: Boolean) {
        viewModelScope.launch {
            sessionPreferences.setBiometricEnabled(enabled)
        }
    }

    /**
     * Notifica al ViewModel el resultado de la consulta de disponibilidad
     * que hace la Activity al `BiometricManager`. Separamos esto del
     * ViewModel porque [android.content.Context] expuesto al ViewModel no
     * basta — `BiometricManager.canAuthenticate()` necesita ejecutarse
     * desde la Activity para que reaccione a permisos en tiempo real.
     */
    fun updateBiometricAvailability(available: Boolean, reason: String?) {
        _availability.value = Availability(available, reason)
    }
}
