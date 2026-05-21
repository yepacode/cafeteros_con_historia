package com.cafeteros.historia.data.repository

import android.content.Context
import android.net.Uri
import com.cafeteros.historia.data.model.FarmProfile
import com.cafeteros.historia.data.remote.firestore.FarmsRemoteDataSource
import com.cafeteros.historia.data.util.ImageBase64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/** Resultado de un upsert de finca. La UI lo usa para toast/navegación. */
sealed class FarmOperationResult {
    data object Success : FarmOperationResult()
    data class Error(val message: String) : FarmOperationResult()
}

/**
 * Punto único de acceso a la finca del caficultor logueado.
 *
 * Orquesta [FarmsRemoteDataSource] + [ImageBase64] (para comprimir la foto
 * principal antes de guardarla). Cualquier ViewModel del módulo de perfil
 * pasa por aquí, nunca toca Firestore directamente.
 */
class FarmRepository(
    private val farmsRemote: FarmsRemoteDataSource,
    private val appContext: Context
) {

    /**
     * Persiste el perfil de finca. Procesa dos fotos independientes:
     *  - [farmPhotoUri]: foto principal de la finca (hero del perfil público).
     *  - [farmerPhotoUri]: retrato/avatar del caficultor (foto de perfil).
     * Si alguna es null, se conserva la foto previa correspondiente.
     */
    suspend fun saveFarm(
        profile: FarmProfile,
        farmPhotoUri: Uri?,
        farmerPhotoUri: Uri? = null
    ): FarmOperationResult = withContext(Dispatchers.IO) {
        runCatching {
            val withImages = profile.copy(
                principalPhotoBase64 = if (farmPhotoUri != null) {
                    ImageBase64.encodeFromUri(appContext, farmPhotoUri)
                        ?: profile.principalPhotoBase64
                } else profile.principalPhotoBase64,
                farmerPhotoBase64 = if (farmerPhotoUri != null) {
                    ImageBase64.encodeFromUri(appContext, farmerPhotoUri)
                        ?: profile.farmerPhotoBase64
                } else profile.farmerPhotoBase64
            )
            farmsRemote.upsert(withImages)
            FarmOperationResult.Success
        }.getOrElse { error ->
            FarmOperationResult.Error(error.message ?: "No se pudo guardar la finca")
        }
    }

    /** Lectura puntual de la finca del caficultor logueado. */
    suspend fun findByCaficultor(caficultorUid: String): FarmProfile? =
        farmsRemote.findByCaficultor(caficultorUid)

    /**
     * [Flow] reactivo de la finca del caficultor [caficultorUid]. Si el doc
     * aún no existe (caficultor recién registrado), emite un perfil vacío
     * con solo el uid — útil para que la UI muestre el form en blanco.
     */
    fun observeMyFarm(caficultorUid: String): Flow<FarmProfile> =
        farmsRemote.observeByCaficultor(caficultorUid)

    /**
     * [Flow] reactivo de TODAS las fincas registradas. Usado por el mapa
     * cafetero del comprador para pintar marcadores.
     */
    fun observeAllFarms(): Flow<List<FarmProfile>> = farmsRemote.observeAll()
}
