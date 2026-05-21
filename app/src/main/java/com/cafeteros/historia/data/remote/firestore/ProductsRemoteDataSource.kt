package com.cafeteros.historia.data.remote.firestore

import com.cafeteros.historia.data.model.CoffeeFormat
import com.cafeteros.historia.data.model.Product
import com.cafeteros.historia.data.model.ProductCategory
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Fuente de datos remota para la colección `/products` de Firestore.
 *
 * Estructura del documento:
 * ```
 * /products/{productId} {
 *   caficultorUid:   String  // uid del dueño; usado para "mis productos"
 *   name:            String
 *   category:        String  // name del enum ProductCategory
 *   shortDescription:String
 *   fullDescription: String
 *   tags:            List<String>
 *   varietyChips:    List<String>
 *   tastingNotes:    List<String>
 *   format:          String  // name del enum CoffeeFormat
 *   weightGrams:     Long
 *   priceCop:        Long
 *   stockUnits:      Long
 *   isOrganic:       Boolean
 *   imageBase64:     String? // JPEG comprimido en Base64 (≤ ~250 KB)
 *   isPaused:        Boolean
 *   createdAt:       Timestamp (server)
 * }
 * ```
 *
 * **Decisión de diseño:** las imágenes se guardan en el mismo documento
 * como Base64 en lugar de Firebase Storage (que requiere plan Blaze). La
 * compresión a ≤200 KB la hace el repositorio antes de llamar aquí.
 */
class ProductsRemoteDataSource(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    private val collection = firestore.collection(COLLECTION_PRODUCTS)

    /**
     * Crea un nuevo documento de producto. Firestore asigna el id; lo
     * devolvemos para que la capa de UI sepa cuál fue creado.
     */
    suspend fun create(product: Product): String {
        val docRef = collection.document()
        docRef.set(product.toFirestoreMap()).await()
        return docRef.id
    }

    /**
     * Actualiza el producto [productId] reemplazando todos los campos por
     * los valores en [product]. Usa `set(..., merge = true)` para no
     * sobreescribir el `createdAt` del servidor.
     */
    suspend fun update(productId: String, product: Product) {
        val updates = product.toFirestoreMap().toMutableMap().apply {
            // No tocamos createdAt en updates.
            remove(FIELD_CREATED_AT)
        }
        collection.document(productId).update(updates).await()
    }

    /** Borra un producto por id. */
    suspend fun delete(productId: String) {
        collection.document(productId).delete().await()
    }

    /**
     * Actualiza únicamente el flag de pausa. Más eficiente que reescribir
     * todo el documento para una acción tan simple.
     */
    suspend fun setPaused(productId: String, paused: Boolean) {
        collection.document(productId).update(FIELD_IS_PAUSED, paused).await()
    }

    /**
     * Actualiza únicamente el stock. Útil desde la pantalla de inventario
     * donde solo se modifica esa cifra con un stepper +/−.
     */
    suspend fun updateStock(productId: String, newStock: Int) {
        collection.document(productId).update(FIELD_STOCK_UNITS, newStock).await()
    }

    /** Lee una sola vez el producto [productId]. Devuelve null si no existe. */
    suspend fun findById(productId: String): Product? {
        val snapshot = collection.document(productId).get().await()
        return snapshot.toProductOrNull()
    }

    /**
     * [Flow] reactivo de todos los productos del caficultor [caficultorUid].
     *
     * **Orden:** Firestore exige un índice compuesto para `where + orderBy`
     * en campos distintos. Para no obligar al usuario a crear índices a mano
     * desde Firebase Console, hacemos el `orderBy` en cliente sobre la lista
     * que recibimos. El volumen por caficultor (decenas de productos) hace
     * que esto sea irrelevante en performance.
     *
     * **Manejo de error:** en lugar de propagar la excepción (que crashearía
     * la app si nadie la atrapa), emitimos lista vacía y lo registramos en
     * logcat. La UI mostrará el estado vacío hasta que el listener se
     * recupere o el usuario salga de la pantalla.
     */
    fun observeByCaficultor(caficultorUid: String): Flow<List<Product>> = callbackFlow {
        val registration: ListenerRegistration = collection
            .whereEqualTo(FIELD_CAFICULTOR_UID, caficultorUid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    android.util.Log.e(TAG, "observeByCaficultor failed", error)
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val products = snapshot?.documents.orEmpty()
                    .mapNotNull { it.toProductOrNull() }
                    .sortedByDescending { it.createdAtEpochMillis }
                trySend(products)
            }
        awaitClose { registration.remove() }
    }

    /**
     * [Flow] reactivo de todos los productos activos (no pausados). Pensado
     * para la pantalla de exploración del comprador. Mismo criterio de
     * orden y manejo de error que [observeByCaficultor].
     */
    fun observeActive(): Flow<List<Product>> = callbackFlow {
        val registration = collection
            .whereEqualTo(FIELD_IS_PAUSED, false)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    android.util.Log.e(TAG, "observeActive failed", error)
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val products = snapshot?.documents.orEmpty()
                    .mapNotNull { it.toProductOrNull() }
                    .sortedByDescending { it.createdAtEpochMillis }
                trySend(products)
            }
        awaitClose { registration.remove() }
    }

    /**
     * Mapeo del modelo de dominio al `Map<String, Any?>` que viaja a
     * Firestore. Los enums se persisten por su `name` (estable, no por
     * `label` que está en español y podría cambiar).
     */
    private fun Product.toFirestoreMap(): Map<String, Any?> = mapOf(
        FIELD_CAFICULTOR_UID to caficultorUid,
        FIELD_NAME to name,
        FIELD_CATEGORY to category.name,
        FIELD_SHORT_DESCRIPTION to shortDescription,
        FIELD_FULL_DESCRIPTION to fullDescription,
        FIELD_TAGS to tags,
        FIELD_VARIETY_CHIPS to varietyChips.toList(),
        FIELD_TASTING_NOTES to tastingNotes.toList(),
        FIELD_FORMAT to format.name,
        FIELD_WEIGHT_GRAMS to weightGrams,
        FIELD_PRICE_COP to priceCop,
        FIELD_STOCK_UNITS to stockUnits,
        FIELD_IS_ORGANIC to isOrganic,
        FIELD_IMAGE_BASE64 to imageBase64,
        FIELD_IS_PAUSED to isPaused,
        FIELD_CREATED_AT to FieldValue.serverTimestamp()
    )

    /**
     * Mapeo inverso: documento → modelo de dominio. Devuelve null si el
     * documento no existe. Para campos opcionales o faltantes usa valores
     * por defecto del modelo, así un documento parcialmente migrado no
     * tira excepción.
     */
    private fun DocumentSnapshot.toProductOrNull(): Product? {
        if (!exists()) return null
        return Product(
            id = id,
            caficultorUid = getString(FIELD_CAFICULTOR_UID).orEmpty(),
            name = getString(FIELD_NAME).orEmpty(),
            category = ProductCategory.fromNameOrDefault(getString(FIELD_CATEGORY)),
            shortDescription = getString(FIELD_SHORT_DESCRIPTION).orEmpty(),
            fullDescription = getString(FIELD_FULL_DESCRIPTION).orEmpty(),
            tags = (get(FIELD_TAGS) as? List<*>)?.filterIsInstance<String>().orEmpty(),
            varietyChips = (get(FIELD_VARIETY_CHIPS) as? List<*>)
                ?.filterIsInstance<String>()?.toSet().orEmpty(),
            tastingNotes = (get(FIELD_TASTING_NOTES) as? List<*>)
                ?.filterIsInstance<String>()?.toSet().orEmpty(),
            format = CoffeeFormat.fromNameOrDefault(getString(FIELD_FORMAT)),
            weightGrams = getLong(FIELD_WEIGHT_GRAMS)?.toInt() ?: 250,
            priceCop = getLong(FIELD_PRICE_COP)?.toInt() ?: 0,
            stockUnits = getLong(FIELD_STOCK_UNITS)?.toInt() ?: 0,
            isOrganic = getBoolean(FIELD_IS_ORGANIC) ?: false,
            imageBase64 = getString(FIELD_IMAGE_BASE64),
            isPaused = getBoolean(FIELD_IS_PAUSED) ?: false,
            createdAtEpochMillis = getTimestamp(FIELD_CREATED_AT)?.toDate()?.time
                ?: System.currentTimeMillis()
        )
    }

    private companion object {
        const val TAG = "ProductsRemoteDS"
        const val COLLECTION_PRODUCTS = "products"
        const val FIELD_CAFICULTOR_UID = "caficultorUid"
        const val FIELD_NAME = "name"
        const val FIELD_CATEGORY = "category"
        const val FIELD_SHORT_DESCRIPTION = "shortDescription"
        const val FIELD_FULL_DESCRIPTION = "fullDescription"
        const val FIELD_TAGS = "tags"
        const val FIELD_VARIETY_CHIPS = "varietyChips"
        const val FIELD_TASTING_NOTES = "tastingNotes"
        const val FIELD_FORMAT = "format"
        const val FIELD_WEIGHT_GRAMS = "weightGrams"
        const val FIELD_PRICE_COP = "priceCop"
        const val FIELD_STOCK_UNITS = "stockUnits"
        const val FIELD_IS_ORGANIC = "isOrganic"
        const val FIELD_IMAGE_BASE64 = "imageBase64"
        const val FIELD_IS_PAUSED = "isPaused"
        const val FIELD_CREATED_AT = "createdAt"
    }
}
