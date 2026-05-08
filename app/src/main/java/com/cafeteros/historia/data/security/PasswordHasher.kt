package com.cafeteros.historia.data.security

import java.security.MessageDigest

/**
 * Utilidad para hashear contraseñas antes de persistirlas.
 *
 * Usa SHA-256 puro (sin salt) por simplicidad mientras la app es local. Para
 * producción real con backend se debe migrar a un esquema con salt + KDF
 * (BCrypt, Argon2 o PBKDF2). El plan está documentado para el futuro:
 *
 * 1. Agregar `salt` por usuario en [com.cafeteros.historia.data.local.database.entities.UserEntity].
 * 2. Reemplazar [hash] por una función `hash(password, salt)` con BCrypt.
 * 3. Migrar contraseñas existentes la próxima vez que el usuario inicie sesión.
 */
object PasswordHasher {

    /**
     * Calcula el hash SHA-256 de [plainPassword] y lo devuelve en hexadecimal
     * (64 caracteres en minúscula).
     */
    fun hash(plainPassword: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = digest.digest(plainPassword.toByteArray(Charsets.UTF_8))
        return bytes.joinToString(separator = "") { byte ->
            "%02x".format(byte)
        }
    }

    /** Compara un texto plano contra un hash previamente almacenado. */
    fun matches(plainPassword: String, expectedHash: String): Boolean =
        hash(plainPassword) == expectedHash
}
