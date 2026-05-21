package com.cafeteros.historia.ui.features.farmer_profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cafeteros.historia.CafeterosApplication
import com.cafeteros.historia.data.model.FarmProfile
import com.cafeteros.historia.data.repository.FarmOperationResult
import com.cafeteros.historia.data.repository.FarmRepository
import com.cafeteros.historia.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel de [CertificationsActivity].
 *
 * Observa las certificaciones actuales de la finca y permite agregar/quitar
 * cada certificación de forma individual. Cada cambio dispara un `upsert`
 * al repositorio para que la persistencia sea inmediata (no hay botón
 * "Guardar" — el flujo es como un toggle).
 *
 * **Concurrencia:** si el usuario tap-tap-tap muchas certificaciones rápido,
 * cada llamada se enlaza al [viewModelScope] y se procesan en orden. La
 * última escritura "gana" — Firestore las consolida.
 */
@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class CertificationsViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as CafeterosApplication
    private val farmRepository: FarmRepository = app.farmRepository
    private val userRepository: UserRepository = app.userRepository

    /** Snapshot del último perfil leído. Se usa para construir el upsert. */
    private val currentFarm: StateFlow<FarmProfile> = flowOf(userRepository.currentUid())
        .flatMapLatest { uid ->
            if (uid == null) flowOf(FarmProfile())
            else farmRepository.observeMyFarm(uid)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FarmProfile()
        )

    /** Lista observada por la UI. */
    val activeCertifications: StateFlow<List<String>> = currentFarm
        .map { it.certifications }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving

    private val _toast = MutableStateFlow<String?>(null)
    val toast: StateFlow<String?> = _toast

    /** Agrega una certificación a la finca (si no estaba ya). */
    fun add(certification: String) {
        val current = currentFarm.value
        if (certification in current.certifications) return
        persist(current.copy(certifications = current.certifications + certification))
    }

    /** Quita una certificación de la finca. */
    fun remove(certification: String) {
        val current = currentFarm.value
        if (certification !in current.certifications) return
        persist(current.copy(certifications = current.certifications - certification))
    }

    private fun persist(updated: FarmProfile) {
        if (_isSaving.value) return
        _isSaving.value = true
        viewModelScope.launch {
            val uid = userRepository.currentUid()
            if (uid == null) {
                _isSaving.value = false
                _toast.value = "Tu sesión expiró"
                return@launch
            }
            val result = farmRepository.saveFarm(
                profile = updated.copy(caficultorUid = uid),
                farmPhotoUri = null,
                farmerPhotoUri = null
            )
            _isSaving.value = false
            if (result is FarmOperationResult.Error) {
                _toast.value = result.message
            }
        }
    }

    fun consumeToast() {
        _toast.value = null
    }
}
