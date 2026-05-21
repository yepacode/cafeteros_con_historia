package com.cafeteros.historia.data.remote.firestore

import com.cafeteros.historia.data.model.FarmProfile
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Fuente de datos remota para la colección `/farms` de Firestore.
 *
 * Cada documento representa la finca de un caficultor, indexado por el
 * `uid` de Firebase Auth (uno-a-uno con `/users/{uid}` para usuarios de
 * rol CAFICULTOR).
 *
 * Estructura del documento:
 * ```
 * /farms/{caficultorUid} {
 *   caficultorUid:        String  // == id del documento
 *   name:                 String
 *   title:                String
 *   highlight:            String
 *   story:                String
 *   region:               String
 *   altitudeMeters:       Long
 *   areaHectares:         Long
 *   varieties:            List<String>
 *   shadeType:            String
 *   processSteps:         List<String>
 *   certifications:       List<String>
 *   quote:                String
 *   videoUrl:             String?
 *   principalPhotoBase64: String? // JPEG comprimido (mismo helper que productos)
 *   updatedAt:            Timestamp (server)
 * }
 * ```
 */
class FarmsRemoteDataSource(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    private val collection = firestore.collection(COLLECTION_FARMS)

    /**
     * Crea o actualiza la finca del caficultor [caficultorUid]. Usa `set`
     * con merge para no perder campos que el formulario no haya tocado.
     */
    suspend fun upsert(profile: FarmProfile) {
        require(profile.caficultorUid.isNotBlank()) {
            "FarmProfile.caficultorUid es obligatorio para guardar la finca."
        }
        collection.document(profile.caficultorUid)
            .set(profile.toFirestoreMap(), com.google.firebase.firestore.SetOptions.merge())
            .await()
    }

    /** Lee una sola vez la finca del caficultor [caficultorUid]. */
    suspend fun findByCaficultor(caficultorUid: String): FarmProfile? {
        val snapshot = collection.document(caficultorUid).get().await()
        return snapshot.toFarmOrNull()
    }

    /**
     * [Flow] reactivo del documento de finca del caficultor [caficultorUid].
     * Si el documento aún no existe (caficultor nuevo que no ha llenado el
     * perfil), emite un [FarmProfile] vacío con solo el uid, para que la UI
     * pueda renderizar el form en blanco.
     */
    fun observeByCaficultor(caficultorUid: String): Flow<FarmProfile> = callbackFlow {
        val registration: ListenerRegistration = collection.document(caficultorUid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    android.util.Log.e(TAG, "observeByCaficultor failed", error)
                    trySend(FarmProfile(caficultorUid = caficultorUid))
                    return@addSnapshotListener
                }
                val farm = snapshot?.toFarmOrNull()
                    ?: FarmProfile(caficultorUid = caficultorUid)
                trySend(farm)
            }
        awaitClose { registration.remove() }
    }

    /**
     * Flow de TODAS las fincas registradas. Pensado para el mapa cafetero
     * que muestra cada caficultor como marcador en su latitud/longitud.
     * El cliente filtra las que no tengan coordenadas.
     */
    fun observeAll(): Flow<List<FarmProfile>> = callbackFlow {
        val registration: ListenerRegistration = collection
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    android.util.Log.e(TAG, "observeAll failed", error)
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val farms = snapshot?.documents.orEmpty()
                    .mapNotNull { it.toFarmOrNull() }
                trySend(farms)
            }
        awaitClose { registration.remove() }
    }

    /**
     * Mapeo del modelo de dominio al `Map<String, Any?>` que viaja a
     * Firestore. Sobrescribimos `updatedAt` con timestamp del servidor.
     */
    private fun FarmProfile.toFirestoreMap(): Map<String, Any?> = mapOf(
        FIELD_CAFICULTOR_UID to caficultorUid,
        FIELD_NAME to name,
        FIELD_TITLE to title,
        FIELD_HIGHLIGHT to highlight,
        FIELD_STORY to story,
        FIELD_REGION to region,
        FIELD_ALTITUDE_M to altitudeMeters,
        FIELD_AREA_HA to areaHectares,
        FIELD_VARIETIES to varieties,
        FIELD_SHADE_TYPE to shadeType,
        FIELD_PROCESS_STEPS to processSteps,
        FIELD_CERTIFICATIONS to certifications,
        FIELD_QUOTE to quote,
        FIELD_VIDEO_URL to videoUrl,
        FIELD_PHOTO_BASE64 to principalPhotoBase64,
        FIELD_FARMER_PHOTO_BASE64 to farmerPhotoBase64,
        FIELD_LATITUDE to latitude,
        FIELD_LONGITUDE to longitude,
        FIELD_UPDATED_AT to FieldValue.serverTimestamp()
    )

    /** Mapeo inverso documento → modelo. Devuelve null si el doc no existe. */
    private fun DocumentSnapshot.toFarmOrNull(): FarmProfile? {
        if (!exists()) return null
        return FarmProfile(
            caficultorUid = id,
            name = getString(FIELD_NAME).orEmpty(),
            title = getString(FIELD_TITLE).orEmpty(),
            highlight = getString(FIELD_HIGHLIGHT).orEmpty(),
            story = getString(FIELD_STORY).orEmpty(),
            region = getString(FIELD_REGION).orEmpty(),
            altitudeMeters = getLong(FIELD_ALTITUDE_M)?.toInt() ?: 0,
            areaHectares = getLong(FIELD_AREA_HA)?.toInt() ?: 0,
            varieties = (get(FIELD_VARIETIES) as? List<*>)
                ?.filterIsInstance<String>().orEmpty(),
            shadeType = getString(FIELD_SHADE_TYPE).orEmpty(),
            processSteps = (get(FIELD_PROCESS_STEPS) as? List<*>)
                ?.filterIsInstance<String>().orEmpty(),
            certifications = (get(FIELD_CERTIFICATIONS) as? List<*>)
                ?.filterIsInstance<String>().orEmpty(),
            quote = getString(FIELD_QUOTE).orEmpty(),
            videoUrl = getString(FIELD_VIDEO_URL),
            principalPhotoBase64 = getString(FIELD_PHOTO_BASE64),
            farmerPhotoBase64 = getString(FIELD_FARMER_PHOTO_BASE64),
            latitude = getDouble(FIELD_LATITUDE),
            longitude = getDouble(FIELD_LONGITUDE),
            updatedAtEpochMillis = getTimestamp(FIELD_UPDATED_AT)?.toDate()?.time
                ?: System.currentTimeMillis()
        )
    }

    private companion object {
        const val TAG = "FarmsRemoteDS"
        const val COLLECTION_FARMS = "farms"
        const val FIELD_CAFICULTOR_UID = "caficultorUid"
        const val FIELD_NAME = "name"
        const val FIELD_TITLE = "title"
        const val FIELD_HIGHLIGHT = "highlight"
        const val FIELD_STORY = "story"
        const val FIELD_REGION = "region"
        const val FIELD_ALTITUDE_M = "altitudeMeters"
        const val FIELD_AREA_HA = "areaHectares"
        const val FIELD_VARIETIES = "varieties"
        const val FIELD_SHADE_TYPE = "shadeType"
        const val FIELD_PROCESS_STEPS = "processSteps"
        const val FIELD_CERTIFICATIONS = "certifications"
        const val FIELD_QUOTE = "quote"
        const val FIELD_VIDEO_URL = "videoUrl"
        const val FIELD_PHOTO_BASE64 = "principalPhotoBase64"
        const val FIELD_FARMER_PHOTO_BASE64 = "farmerPhotoBase64"
        const val FIELD_LATITUDE = "latitude"
        const val FIELD_LONGITUDE = "longitude"
        const val FIELD_UPDATED_AT = "updatedAt"
    }
}
