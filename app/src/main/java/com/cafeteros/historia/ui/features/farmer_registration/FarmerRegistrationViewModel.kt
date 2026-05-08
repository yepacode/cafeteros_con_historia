package com.cafeteros.historia.ui.features.farmer_registration

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import com.cafeteros.historia.CafeterosApplication
import com.cafeteros.historia.data.repository.RegisterResult
import com.cafeteros.historia.ui.features.auth.components.UserType
import com.cafeteros.historia.ui.features.farmer_registration.model.CoffeeProcess
import com.cafeteros.historia.ui.features.farmer_registration.model.CoffeeVariety
import com.cafeteros.historia.ui.features.farmer_registration.model.FarmerRegistrationFormState
import com.cafeteros.historia.ui.features.farmer_registration.model.FarmerRegistrationStep
import com.cafeteros.historia.ui.features.farmer_registration.model.Gender
import com.cafeteros.historia.ui.features.farmer_registration.model.HarvestMethod
import com.cafeteros.historia.ui.features.farmer_registration.model.IdentificationType
import com.cafeteros.historia.ui.features.farmer_registration.model.MandatoryDocumentType
import com.cafeteros.historia.ui.features.farmer_registration.model.OptionalDocumentType
import com.cafeteros.historia.ui.features.farmer_registration.model.PhotoSlot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel del flujo de registro del caficultor.
 *
 * Centraliza:
 *  - Estado del formulario completo ([formState]) — sobrevive rotaciones de pantalla.
 *  - Paso actual del flujo ([currentStep]).
 *  - Generación y verificación del OTP del correo (modo demo).
 *
 * Cuando exista backend, el método [requestEmailOtp] enviará un POST al
 * servidor en vez de generar el código localmente; [verifyEmailOtp] llamará
 * a otro endpoint que valide el código contra lo enviado.
 */
class FarmerRegistrationViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository = (application as CafeterosApplication).userRepository

    private val _formState = MutableStateFlow(FarmerRegistrationFormState())
    val formState: StateFlow<FarmerRegistrationFormState> = _formState.asStateFlow()

    private val _currentStep = MutableStateFlow(FarmerRegistrationStep.PersonalData)
    val currentStep: StateFlow<FarmerRegistrationStep> = _currentStep.asStateFlow()

    /**
     * Código OTP generado y "enviado" al correo. En modo demo se guarda en
     * memoria; en producción este valor lo conoce solo el servidor.
     */
    private var pendingEmailOtp: String? = null

    // ── Setters de campos del formulario ────────────────────────────────────

    fun setIdentificationType(type: IdentificationType) {
        _formState.value = _formState.value.copy(identificationType = type)
    }

    fun setDocumentNumber(value: String) {
        _formState.value = _formState.value.copy(documentNumber = value.filter { it.isDigit() })
    }

    fun setDocumentIssuedAt(epochMillis: Long?) {
        _formState.value = _formState.value.copy(documentIssuedAtEpochMillis = epochMillis)
    }

    fun setFirstNames(value: String) {
        _formState.value = _formState.value.copy(firstNames = value)
    }

    fun setLastNames(value: String) {
        _formState.value = _formState.value.copy(lastNames = value)
    }

    fun setBirthDate(epochMillis: Long?) {
        _formState.value = _formState.value.copy(birthDateEpochMillis = epochMillis)
    }

    fun setGender(gender: Gender) {
        _formState.value = _formState.value.copy(gender = gender)
    }

    fun setPhoneNumber(value: String) {
        _formState.value = _formState.value.copy(phoneNumber = value.filter { it.isDigit() })
    }

    fun setEmail(value: String) {
        // Si el correo cambia, se invalida cualquier verificación previa.
        _formState.value = _formState.value.copy(email = value, emailVerified = false)
        pendingEmailOtp = null
    }

    fun setDepartmentCode(code: String?) {
        // Cambiar de departamento debe limpiar el municipio elegido para evitar
        // mezclas (ej. ciudad de Antioquia con departamento de Cundinamarca).
        _formState.value = _formState.value.copy(departmentCode = code, cityCode = null)
    }

    fun setCityCode(code: String?) {
        _formState.value = _formState.value.copy(cityCode = code)
    }

    fun setResidenceAddress(value: String) {
        _formState.value = _formState.value.copy(residenceAddress = value)
    }

    // ── Setters del paso 2 (Finca) ──────────────────────────────────────────

    fun setFarmName(value: String) {
        _formState.value = _formState.value.copy(farmName = value)
    }

    /**
     * Filtra a dígitos para mantener el campo numérico, pero acepta string
     * para no perder estados intermedios mientras el usuario escribe.
     */
    fun setYearsOfCoffeeExperience(value: String) {
        _formState.value = _formState.value.copy(
            yearsOfCoffeeExperience = value.filter { it.isDigit() }
        )
    }

    fun setAltitudeMasl(value: String) {
        _formState.value = _formState.value.copy(
            altitudeMasl = value.filter { it.isDigit() }
        )
    }

    fun setTotalAreaHectares(value: String) {
        _formState.value = _formState.value.copy(
            totalAreaHectares = value.sanitizeAsDecimal()
        )
    }

    fun setCultivatedAreaHectares(value: String) {
        _formState.value = _formState.value.copy(
            cultivatedAreaHectares = value.sanitizeAsDecimal()
        )
    }

    fun setEstimatedAnnualProductionKg(value: String) {
        _formState.value = _formState.value.copy(
            estimatedAnnualProductionKg = value.filter { it.isDigit() }
        )
    }

    fun toggleCultivatedVariety(variety: CoffeeVariety) {
        val current = _formState.value.cultivatedVarieties
        val updated = if (variety in current) current - variety else current + variety
        _formState.value = _formState.value.copy(cultivatedVarieties = updated)
    }

    fun toggleProcess(process: CoffeeProcess) {
        val current = _formState.value.processes
        val updated = if (process in current) current - process else current + process
        _formState.value = _formState.value.copy(processes = updated)
    }

    fun setHarvestMethod(method: HarvestMethod) {
        _formState.value = _formState.value.copy(harvestMethod = method)
    }

    fun setHasAwards(checked: Boolean) {
        _formState.value = _formState.value.copy(hasAwards = checked)
    }

    // ── Setters del paso 4 (Verificación documental) ────────────────────────

    /**
     * Guarda la URI del documento obligatorio dado. Si pasa `null` se borra
     * el documento previamente seleccionado para ese tipo.
     *
     * Cuando se sube un documento de Cámara de Comercio, automáticamente se
     * desactiva la marca "No aplica" para evitar estados inconsistentes.
     */
    fun setMandatoryDocument(type: MandatoryDocumentType, uri: Uri?) {
        val current = _formState.value
        val updatedMap = if (uri == null) {
            current.mandatoryDocuments - type
        } else {
            current.mandatoryDocuments + (type to uri)
        }
        val turnsOffNotApplicable =
            type == MandatoryDocumentType.CHAMBER_OF_COMMERCE && uri != null
        _formState.value = current.copy(
            mandatoryDocuments = updatedMap,
            chamberOfCommerceNotApplicable = if (turnsOffNotApplicable) {
                false
            } else {
                current.chamberOfCommerceNotApplicable
            }
        )
    }

    /**
     * Alterna la marca "No aplica" del Certificado de Cámara de Comercio.
     * Si se activa, también borra el documento que pudiera estar previamente
     * subido para ese tipo (no tiene sentido tener ambos).
     */
    fun toggleChamberOfCommerceNotApplicable() {
        val current = _formState.value
        val nextValue = !current.chamberOfCommerceNotApplicable
        _formState.value = current.copy(
            chamberOfCommerceNotApplicable = nextValue,
            mandatoryDocuments = if (nextValue) {
                current.mandatoryDocuments - MandatoryDocumentType.CHAMBER_OF_COMMERCE
            } else {
                current.mandatoryDocuments
            }
        )
    }

    /** Guarda o borra la URI de un documento opcional. */
    fun setOptionalDocument(type: OptionalDocumentType, uri: Uri?) {
        val current = _formState.value
        val updatedMap = if (uri == null) {
            current.optionalDocuments - type
        } else {
            current.optionalDocuments + (type to uri)
        }
        _formState.value = current.copy(optionalDocuments = updatedMap)
    }

    fun setPhotoForSlot(slot: PhotoSlot, uri: Uri?) {
        _formState.value = when (slot) {
            PhotoSlot.DOCUMENT_FRONT ->
                _formState.value.copy(documentFrontPhotoUri = uri)
            PhotoSlot.DOCUMENT_BACK ->
                _formState.value.copy(documentBackPhotoUri = uri)
            PhotoSlot.SELFIE_WITH_DOCUMENT ->
                _formState.value.copy(selfieWithDocumentUri = uri)
        }
    }

    // ── Verificación de correo (OTP modo demo) ──────────────────────────────

    /**
     * "Envía" un OTP al correo del usuario.
     *
     * **Modo demo**: usa un código FIJO [DEMO_OTP_CODE] para facilitar pruebas.
     * El usuario siempre puede escribir `123456` y verificar.
     *
     * Cuando exista backend real:
     *  ```
     *  // TODO backend: reemplazar por
     *  //   emailService.sendOtp(formState.value.email)
     *  // y NO devolver el código localmente. El código real solo lo conoce
     *  // el servidor; el usuario lo recibe por correo.
     *  ```
     *
     * @return el código a "mostrar" (en demo). En producción será `Unit`.
     */
    fun requestEmailOtp(): String {
        pendingEmailOtp = DEMO_OTP_CODE
        return DEMO_OTP_CODE
    }

    /**
     * Valida el código OTP que el usuario escribió contra el último generado.
     *
     * @return true si coincide; false en caso contrario.
     */
    fun verifyEmailOtp(input: String): Boolean {
        val matches = pendingEmailOtp != null && input == pendingEmailOtp
        if (matches) {
            _formState.value = _formState.value.copy(emailVerified = true)
            pendingEmailOtp = null
        }
        return matches
    }

    // ── Navegación entre pasos ──────────────────────────────────────────────

    /** Avanza al siguiente paso si lo hay; idempotente si ya está en el último. */
    fun goToNextStep() {
        val nextOrdinal = _currentStep.value.ordinal + 1
        if (nextOrdinal < FarmerRegistrationStep.entries.size) {
            _currentStep.value = FarmerRegistrationStep.entries[nextOrdinal]
        }
    }

    /**
     * Persiste al caficultor en la base de datos local con los datos mínimos
     * del paso 1 (correo + nombre + teléfono) y deja sesión iniciada.
     *
     * Se invoca al pulsar "Continuar" en el paso 4 (Verificación documental),
     * justo antes de mostrar la pantalla "Estamos revisando tu finca".
     *
     * Detalles intencionalmente simples mientras no hay backend:
     *  - La contraseña queda como placeholder fijo (ver [DEMO_PASSWORD]).
     *    Cuando llegue el backend habrá un paso explícito de "crear contraseña"
     *    o un flujo OTP/biometría que reemplace esto.
     *  - Solo se guarda lo que la tabla `users` tiene hoy. El resto del
     *    formulario (finca, variedades, fotos…) se descarta — irá al backend
     *    cuando exista el endpoint correspondiente.
     *
     * @return [RegisterResult] para que la UI decida qué mostrar (éxito,
     *  correo duplicado, error inesperado).
     */
    suspend fun registerCurrentUser(): RegisterResult {
        val state = _formState.value
        val fullName = "${state.firstNames.trim()} ${state.lastNames.trim()}".trim()
        return userRepository.register(
            email = state.email,
            name = fullName,
            phone = state.phoneNumber,
            plainPassword = DEMO_PASSWORD,
            userType = UserType.CAFICULTOR
        )
    }

    /** Retrocede al paso anterior si lo hay; devuelve true si retrocedió. */
    fun goToPreviousStep(): Boolean {
        val prevOrdinal = _currentStep.value.ordinal - 1
        return if (prevOrdinal >= 0) {
            _currentStep.value = FarmerRegistrationStep.entries[prevOrdinal]
            true
        } else {
            false
        }
    }
}

/**
 * Código OTP de demo: siempre "123456".
 *
 * Permite probar el flujo de verificación de correo sin tener que mirar el
 * Toast cada vez. Quitar esta constante y volver a generar un código aleatorio
 * cuando se conecte el backend de envío de correos.
 */
private const val DEMO_OTP_CODE: String = "123456"

/**
 * Contraseña placeholder que se asigna al caficultor en modo demo. Permite
 * crear la fila en la tabla `users` sin pedirle al usuario un password (el
 * flujo del caficultor no tiene paso de contraseña). Quitar cuando el
 * backend reemplace este flujo con auth real.
 */
private const val DEMO_PASSWORD: String = "caficultor123"

/**
 * Limpia un string que el usuario está escribiendo para que solo permanezcan
 * dígitos y un único punto decimal. No lo convierte a Double aún (eso lo
 * hará el repositorio al persistir) — así no perdemos estados intermedios
 * como "5." o "5.5" mientras el usuario tipea.
 */
private fun String.sanitizeAsDecimal(): String {
    val sanitized = this.replace(",", ".").filter { it.isDigit() || it == '.' }
    val firstDot = sanitized.indexOf('.')
    return if (firstDot == -1) {
        sanitized
    } else {
        val before = sanitized.substring(0, firstDot + 1)
        val after = sanitized.substring(firstDot + 1).filter { it != '.' }
        before + after
    }
}
