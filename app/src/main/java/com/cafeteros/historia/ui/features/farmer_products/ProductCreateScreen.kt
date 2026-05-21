package com.cafeteros.historia.ui.features.farmer_products

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cafeteros.historia.ui.features.farmer_products.model.ProductCreationStep
import com.cafeteros.historia.ui.features.farmer_products.steps.ProductBasicsStep
import com.cafeteros.historia.ui.features.farmer_products.steps.ProductPreviewStep
import com.cafeteros.historia.ui.features.farmer_products.steps.ProductPricingStep

/**
 * Host del wizard de creación/edición de producto. Despacha al step actual
 * con `AnimatedContent` y un `when` exhaustivo sobre [ProductCreationStep].
 *
 * @param editingProductId si es no-null, entra en modo edición y precarga
 *  el formulario con los datos del producto desde Firestore.
 * @param onClose callback para cerrar la activity (back en el primer paso).
 * @param onPublished callback al persistir con éxito. Recibe el id y el
 *  nombre del producto para que la activity pueda navegar a la pantalla de
 *  confirmación sin necesidad del modelo completo.
 * @param onPublishError callback al fallar la persistencia. La activity
 *  muestra un toast con el mensaje.
 */
@Composable
fun ProductCreateScreen(
    modifier: Modifier = Modifier,
    editingProductId: String?,
    onClose: () -> Unit,
    onPublished: (productId: String, productName: String) -> Unit,
    onPublishError: (message: String) -> Unit
) {
    val viewModel: ProductCreateViewModel = viewModel()
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val currentStep by viewModel.currentStep.collectAsStateWithLifecycle()
    val publishOutcome by viewModel.publishOutcome.collectAsStateWithLifecycle()
    val isEditing = editingProductId != null

    // Pre-cargar datos del producto al entrar en modo edición.
    LaunchedEffect(editingProductId) {
        if (editingProductId != null) viewModel.loadForEditing(editingProductId)
    }

    // Reaccionar al resultado de publish() emitido por el ViewModel.
    LaunchedEffect(publishOutcome) {
        when (val outcome = publishOutcome) {
            is PublishOutcome.Success -> {
                onPublished(outcome.productId, outcome.productName)
                viewModel.consumePublishOutcome()
            }
            is PublishOutcome.Error -> {
                onPublishError(outcome.message)
                viewModel.consumePublishOutcome()
            }
            null -> Unit
        }
    }

    val handleBack: () -> Unit = {
        val moved = viewModel.goToPreviousStep()
        if (!moved) onClose()
    }

    AnimatedContent(
        targetState = currentStep,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "productCreationStep",
        modifier = modifier
    ) { step ->
        when (step) {
            ProductCreationStep.Basics -> ProductBasicsStep(
                state = formState,
                isEditing = isEditing,
                onBack = handleBack,
                onContinue = viewModel::goToNextStep,
                onPhotoSelected = viewModel::setPhotoUri,
                onNameChange = viewModel::setName,
                onCategoryChange = viewModel::setCategory,
                onShortDescriptionChange = viewModel::setShortDescription,
                onFullDescriptionChange = viewModel::setFullDescription,
                onTagInputChange = viewModel::setTagsInput,
                onTagSubmit = viewModel::commitTagFromInput,
                onTagRemove = viewModel::removeTag,
                onToggleVariety = viewModel::toggleVariety,
                onToggleTastingNote = viewModel::toggleTastingNote
            )

            ProductCreationStep.Pricing -> ProductPricingStep(
                state = formState,
                isEditing = isEditing,
                onBack = handleBack,
                onContinue = viewModel::goToNextStep,
                onFormatChange = viewModel::setFormat,
                onWeightChange = viewModel::setWeightGrams,
                onPriceChange = viewModel::setPriceCopInput,
                onStockChange = viewModel::setStockUnitsInput,
                onOrganicChange = viewModel::setOrganic
            )

            ProductCreationStep.Preview -> ProductPreviewStep(
                state = formState,
                isEditing = isEditing,
                onBack = handleBack,
                onPublish = viewModel::publish
            )
        }
    }
}
