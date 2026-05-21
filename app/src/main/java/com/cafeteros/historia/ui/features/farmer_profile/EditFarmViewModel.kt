package com.cafeteros.historia.ui.features.farmer_profile

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cafeteros.historia.CafeterosApplication
import com.cafeteros.historia.data.model.FarmProfile
import com.cafeteros.historia.data.repository.FarmOperationResult
import com.cafeteros.historia.data.repository.FarmRepository
import com.cafeteros.historia.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Resultado expuesto a la Activity tras pulsar "Guardar".
 *
 * La Activity observa este flow para decidir si cerrar la pantalla
 * (Success) o mostrar un toast de error.
 */
sealed class FarmSaveOutcome {
    data object Success : FarmSaveOutcome()
    data class Error(val message: String) : FarmSaveOutcome()
}

/**
 * ViewModel de [EditFarmActivity].
 *
 * Al instanciarse carga puntualmente la finca del caficultor logueado para
 * que el formulario arranque con los datos guardados. No usa un Flow
 * reactivo aquí porque cada cambio de Firestore mientras el usuario está
 * editando sobrescribiría sus cambios locales — esto es una pantalla de
 * edición, no de lectura.
 *
 * Cuando el usuario guarda, construye un [FarmProfile] con todos los
 * campos del form y los persiste vía [FarmRepository]. Si el usuario subió
 * una foto nueva, la pasa como [Uri] y el repo se encarga de comprimirla
 * a Base64 antes de mandarla a Firestore.
 */
class EditFarmViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as CafeterosApplication
    private val farmRepository: FarmRepository = app.farmRepository
    private val userRepository: UserRepository = app.userRepository

    /** Perfil cargado al entrar (snapshot inicial para poblar el form). */
    private val _initial = MutableStateFlow<FarmProfile?>(null)
    val initial: StateFlow<FarmProfile?> = _initial.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _outcome = MutableStateFlow<FarmSaveOutcome?>(null)
    val outcome: StateFlow<FarmSaveOutcome?> = _outcome.asStateFlow()

    init {
        loadInitialProfile()
    }

    /**
     * Lee la finca actual del caficultor desde Firestore para precargar el
     * form. Si no existe aún (caficultor recién registrado), crea un perfil
     * vacío con solo el uid para que el form arranque limpio.
     */
    private fun loadInitialProfile() {
        viewModelScope.launch {
            val uid = userRepository.currentUid()
            if (uid == null) {
                _initial.value = FarmProfile()
                return@launch
            }
            val farm = farmRepository.findByCaficultor(uid)
                ?: FarmProfile(caficultorUid = uid)
            _initial.value = farm
        }
    }

    /**
     * Persiste el [profile] actual con las dos fotos opcionales del form
     * (finca + caficultor). Si alguna URI es null, conserva la foto previa
     * correspondiente.
     */
    fun save(profile: FarmProfile, farmPhotoUri: Uri?, farmerPhotoUri: Uri?) {
        if (_isSaving.value) return
        _isSaving.value = true

        viewModelScope.launch {
            val uid = userRepository.currentUid()
            if (uid == null) {
                _isSaving.value = false
                _outcome.value = FarmSaveOutcome.Error(
                    "Tu sesión expiró. Inicia sesión otra vez para guardar tu finca."
                )
                return@launch
            }
            val result = farmRepository.saveFarm(
                profile = profile.copy(caficultorUid = uid),
                farmPhotoUri = farmPhotoUri,
                farmerPhotoUri = farmerPhotoUri
            )
            _isSaving.value = false
            _outcome.value = when (result) {
                is FarmOperationResult.Success -> FarmSaveOutcome.Success
                is FarmOperationResult.Error -> FarmSaveOutcome.Error(result.message)
            }
        }
    }

    /** Resetea el resultado tras consumirlo. */
    fun consumeOutcome() {
        _outcome.value = null
    }
}
