package com.cafeteros.historia.ui.features.farmer_products

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.cafeteros.historia.ui.features.farmer_products.model.CoffeeFormat
import com.cafeteros.historia.ui.features.farmer_products.model.Product
import com.cafeteros.historia.ui.features.farmer_products.model.ProductCategory
import com.cafeteros.historia.ui.features.farmer_products.model.ProductCreationStep
import com.cafeteros.historia.ui.features.farmer_products.model.ProductFormState
import com.cafeteros.historia.ui.features.farmer_products.model.ProductsStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel del wizard de creación/edición de producto.
 *
 * Centraliza:
 *  - El [ProductFormState] del producto en construcción.
 *  - El [ProductCreationStep] activo (3 pasos).
 *  - La persistencia final en [ProductsStore] al pulsar "Publicar".
 *
 * No es AndroidViewModel porque por ahora no necesita Application/Context —
 * la store es singleton in-memory. Cuando se conecte Room habrá que pasar
 * [com.cafeteros.historia.CafeterosApplication] como en
 * [com.cafeteros.historia.ui.features.farmer_registration.FarmerRegistrationViewModel].
 */
class ProductCreateViewModel : ViewModel() {

    private val _formState = MutableStateFlow(ProductFormState())
    val formState: StateFlow<ProductFormState> = _formState.asStateFlow()

    private val _currentStep = MutableStateFlow(ProductCreationStep.Basics)
    val currentStep: StateFlow<ProductCreationStep> = _currentStep.asStateFlow()

    /**
     * Pre-carga el formulario con los datos de un producto existente para
     * entrar en modo edición. Idempotente: si se vuelve a llamar con el
     * mismo id, simplemente reemplaza el state.
     */
    fun loadForEditing(productId: String) {
        val existing = ProductsStore.findById(productId) ?: return
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
            photoUri = existing.photoUri
        )
        _currentStep.value = ProductCreationStep.Basics
    }

    // ── Setters del paso 1 (Detalles del producto) ──────────────────────────

    fun setPhotoUri(uri: Uri?) {
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
     * Persiste el producto en [ProductsStore]. Si el formulario es de
     * edición ([ProductFormState.editingProductId] no-null), reemplaza el
     * producto existente; si es creación, lo agrega.
     *
     * @return el [Product] persistido (útil para mostrarlo en
     *  [com.cafeteros.historia.ui.features.farmer_products.ProductPublishedActivity]).
     */
    fun publish(): Product {
        val product = _formState.value.toProduct()
        if (_formState.value.editingProductId != null) {
            ProductsStore.update(product)
        } else {
            ProductsStore.add(product)
        }
        return product
    }
}
