package com.cafeteros.historia.ui.features.farmer_products

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cafeteros.historia.CafeterosApplication
import com.cafeteros.historia.data.model.CoffeeFormat
import com.cafeteros.historia.data.model.ProductCategory
import com.cafeteros.historia.data.repository.ProductOperationResult
import com.cafeteros.historia.data.repository.ProductRepository
import com.cafeteros.historia.data.repository.UserRepository
import com.cafeteros.historia.ui.features.farmer_products.model.ProductCreationStep
import com.cafeteros.historia.ui.features.farmer_products.model.ProductFormState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Resultado de una operación de publicación expuesto a la Activity.
 *
 * La Activity lo consume para decidir si navegar a [ProductPublishedActivity]
 * o mostrar un toast de error.
 */
sealed class PublishOutcome {
    data class Success(val productId: String, val productName: String) : PublishOutcome()
    data class Error(val message: String) : PublishOutcome()
}

/**
 * ViewModel del wizard de creación/edición de producto.
 *
 * Conecta el [ProductFormState] (estado de UI) con el [ProductRepository]
 * (persistencia en Firestore + compresión de imagen). El uid del caficultor
 * se obtiene del [UserRepository] al momento de publicar.
 *
 * **Modo edición:** se activa pasando `editingProductId` no-null a
 * [loadForEditing]. El repo lee el producto, llenamos el formulario y
 * marcamos `editingProductId` para que `publish()` haga `update` en lugar
 * de `create`. La foto previa se guarda como `existingImageBase64` para
 * conservarla si el usuario no sube una nueva.
 */
class ProductCreateViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as CafeterosApplication
    private val productRepository: ProductRepository = app.productRepository
    private val userRepository: UserRepository = app.userRepository

    private val _formState = MutableStateFlow(ProductFormState())
    val formState: StateFlow<ProductFormState> = _formState.asStateFlow()

    private val _currentStep = MutableStateFlow(ProductCreationStep.Basics)
    val currentStep: StateFlow<ProductCreationStep> = _currentStep.asStateFlow()

    private val _isPublishing = MutableStateFlow(false)
    val isPublishing: StateFlow<Boolean> = _isPublishing.asStateFlow()

    private val _publishOutcome = MutableStateFlow<PublishOutcome?>(null)
    val publishOutcome: StateFlow<PublishOutcome?> = _publishOutcome.asStateFlow()

    /**
     * Pre-carga el formulario con los datos de un producto existente para
     * entrar en modo edición. Lee desde Firestore vía repositorio.
     */
    fun loadForEditing(productId: String) {
        viewModelScope.launch {
            val existing = productRepository.findById(productId) ?: return@launch
            _formState.value = ProductFormState(
                editingProductId = existing.id,
                name = existing.name,
                category = existing.category,
                shortDescription = existing.shortDescription,
                fullDescription = existing.fullDescription,
                tagsInput = "",
                tags = existing.tags,
                varietyChips = existing.varietyChips,
                tastingNotes = existing.tastingNotes,
                format = existing.format,
                weightGrams = existing.weightGrams,
                priceCopInput = existing.priceCop.toString(),
                stockUnitsInput = existing.stockUnits.toString(),
                isOrganic = existing.isOrganic,
                photoUri = null,
                existingImageBase64 = existing.imageBase64
            )
            _currentStep.value = ProductCreationStep.Basics
        }
    }

    // ── Setters del paso 1 (Detalles del producto) ──────────────────────────

    fun setPhotoUri(uri: Uri?) {
        android.util.Log.d("ProductCreateVM", "setPhotoUri called with: $uri")
        _formState.value = _formState.value.copy(photoUri = uri)
    }

    fun setName(value: String) {
        _formState.value = _formState.value.copy(name = value)
    }

    fun setCategory(category: ProductCategory) {
        _formState.value = _formState.value.copy(category = category)
    }

    fun setShortDescription(value: String) {
        _formState.value = _formState.value.copy(shortDescription = value)
    }

    fun setFullDescription(value: String) {
        _formState.value = _formState.value.copy(fullDescription = value)
    }

    fun setTagsInput(value: String) {
        _formState.value = _formState.value.copy(tagsInput = value)
    }

    /**
     * Toma el contenido actual de [ProductFormState.tagsInput], lo añade a
     * la lista [ProductFormState.tags] (sin duplicados, sin vacíos) y
     * limpia el campo de input.
     */
    fun commitTagFromInput() {
        val current = _formState.value
        val candidate = current.tagsInput.trim().removePrefix("#")
        if (candidate.isBlank() || candidate in current.tags) {
            _formState.value = current.copy(tagsInput = "")
            return
        }
        _formState.value = current.copy(tags = current.tags + candidate, tagsInput = "")
    }

    fun removeTag(tag: String) {
        _formState.value = _formState.value.copy(tags = _formState.value.tags - tag)
    }

    fun toggleVariety(variety: String) {
        val set = _formState.value.varietyChips
        val updated = if (variety in set) set - variety else set + variety
        _formState.value = _formState.value.copy(varietyChips = updated)
    }

    fun toggleTastingNote(note: String) {
        val set = _formState.value.tastingNotes
        val updated = if (note in set) set - note else set + note
        _formState.value = _formState.value.copy(tastingNotes = updated)
    }

    // ── Setters del paso 2 (Precio e inventario) ────────────────────────────

    fun setFormat(format: CoffeeFormat) {
        _formState.value = _formState.value.copy(format = format)
    }

    fun setWeightGrams(grams: Int) {
        _formState.value = _formState.value.copy(weightGrams = grams)
    }

    fun setPriceCopInput(value: String) {
        _formState.value = _formState.value.copy(priceCopInput = value.filter { it.isDigit() })
    }

    fun setStockUnitsInput(value: String) {
        _formState.value = _formState.value.copy(stockUnitsInput = value.filter { it.isDigit() })
    }

    fun setOrganic(checked: Boolean) {
        _formState.value = _formState.value.copy(isOrganic = checked)
    }

    // ── Navegación entre pasos ──────────────────────────────────────────────

    fun goToNextStep() {
        val nextOrdinal = _currentStep.value.ordinal + 1
        if (nextOrdinal < ProductCreationStep.entries.size) {
            _currentStep.value = ProductCreationStep.entries[nextOrdinal]
        }
    }

    /** Retrocede un paso. Devuelve `true` si pudo, `false` si ya está en el primero. */
    fun goToPreviousStep(): Boolean {
        val prevOrdinal = _currentStep.value.ordinal - 1
        return if (prevOrdinal >= 0) {
            _currentStep.value = ProductCreationStep.entries[prevOrdinal]
            true
        } else {
            false
        }
    }

    /**
     * Persiste el producto en Firestore vía [ProductRepository]. Si está en
     * modo edición hace `update`; si no, `create`. Comprime la foto si el
     * usuario subió una nueva.
     *
     * El resultado se emite en [publishOutcome]; la Activity observa ese
     * flujo y reacciona (navegar o mostrar error).
     */
    fun publish() {
        if (_isPublishing.value) return
        _isPublishing.value = true

        viewModelScope.launch {
            val uid = userRepository.currentUid()
            if (uid == null) {
                _isPublishing.value = false
                _publishOutcome.value = PublishOutcome.Error(
                    "Tu sesión expiró. Inicia sesión otra vez para publicar."
                )
                return@launch
            }

            val state = _formState.value
            android.util.Log.d(
                "ProductCreateVM",
                "publish: photoUri=${state.photoUri}, name='${state.name}', editing=${state.editingProductId}"
            )
            val product = state.toProduct(caficultorUid = uid)
            val result = if (state.editingProductId != null) {
                productRepository.updateProduct(
                    productId = state.editingProductId,
                    product = product,
                    photoUri = state.photoUri
                )
            } else {
                productRepository.createProduct(
                    product = product,
                    photoUri = state.photoUri
                )
            }

            _isPublishing.value = false
            _publishOutcome.value = when (result) {
                is ProductOperationResult.Success ->
                    PublishOutcome.Success(productId = result.productId, productName = product.name)
                is ProductOperationResult.Error ->
                    PublishOutcome.Error(result.message)
            }
        }
    }

    /** Resetea el resultado tras consumirlo (evita re-disparar la navegación). */
    fun consumePublishOutcome() {
        _publishOutcome.value = null
    }
}
