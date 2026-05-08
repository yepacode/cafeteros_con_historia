package com.cafeteros.historia.ui.features.password_recovery.model

/**
 * Pasos del flujo de recuperación de contraseña.
 *
 * Se modela como [sealed class] (no enum) para permitir que cada paso lleve
 * datos asociados en el futuro (ej. código generado, tiempo restante de
 * reenvío, etc.) sin romper el resto del flujo.
 *
 * Orden lógico:
 *  1. [EmailEntry] – la usuaria ingresa su correo y solicita un código.
 *  2. [CodeVerification] – la usuaria ingresa el código de 4 dígitos.
 *  3. [NewPassword] – la usuaria define y confirma su nueva contraseña.
 *  4. [Success] – pantalla de confirmación final.
 */
sealed class PasswordRecoveryStep {

    /** Índice del paso (base 1) usado por el stepper visual. `null` si no aplica. */
    abstract val visibleIndex: Int?

    /** Paso 1: entrada del correo. */
    data object EmailEntry : PasswordRecoveryStep() {
        override val visibleIndex: Int = 1
    }

    /** Paso 2: verificación del código de 4 dígitos. */
    data object CodeVerification : PasswordRecoveryStep() {
        override val visibleIndex: Int = 2
    }

    /** Paso 3: definición de la nueva contraseña. */
    data object NewPassword : PasswordRecoveryStep() {
        override val visibleIndex: Int = 3
    }

    /** Pantalla final de éxito. No muestra stepper. */
    data object Success : PasswordRecoveryStep() {
        override val visibleIndex: Int? = null
    }
}
