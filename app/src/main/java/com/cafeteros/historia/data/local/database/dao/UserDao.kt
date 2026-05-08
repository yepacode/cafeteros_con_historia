package com.cafeteros.historia.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cafeteros.historia.data.local.database.entities.UserEntity

/**
 * Acceso SQL a la tabla `users`.
 *
 * Las operaciones son `suspend` para forzar a llamarlas desde una corutina
 * (no bloquea el hilo principal). Room genera la implementación en tiempo
 * de compilación vía KSP.
 */
@Dao
interface UserDao {

    /**
     * Inserta un usuario nuevo. Si el correo ya existe, [OnConflictStrategy.ABORT]
     * hace que Room lance una excepción (capturada en el repositorio).
     *
     * @return el `id` autogenerado del nuevo usuario.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: UserEntity): Long

    /** Busca un usuario por correo. Devuelve `null` si no existe. */
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun findByEmail(email: String): UserEntity?

    /** Busca un usuario por id. Devuelve `null` si no existe. */
    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun findById(id: Long): UserEntity?

    /**
     * Actualiza la contraseña de un usuario identificado por su correo.
     *
     * @return cantidad de filas modificadas (0 si el correo no existe, 1 si se actualizó).
     */
    @Query("UPDATE users SET password_hash = :newPasswordHash WHERE email = :email")
    suspend fun updatePasswordByEmail(email: String, newPasswordHash: String): Int

    /** Útil para tests / debug: borra todos los usuarios. */
    @Query("DELETE FROM users")
    suspend fun deleteAll()
}
