package com.cafeteros.historia.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cafeteros.historia.data.local.database.entities.RoleEntity

/**
 * Acceso SQL a la tabla `roles`.
 *
 * Las operaciones son `suspend` para forzar a llamarlas desde una corutina
 * (no bloquean el hilo principal). Room genera la implementación vía KSP.
 */
@Dao
interface RoleDao {

    /**
     * Inserta una lista de roles. Usa [OnConflictStrategy.IGNORE] para que la
     * siembra inicial sea idempotente: si por algún motivo el callback se
     * ejecuta dos veces, no fallará por duplicados.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(roles: List<RoleEntity>)

    /** Lista todos los roles ordenados por id ascendente. */
    @Query("SELECT * FROM roles ORDER BY id ASC")
    suspend fun getAll(): List<RoleEntity>

    /** Busca un rol por id. Devuelve `null` si no existe. */
    @Query("SELECT * FROM roles WHERE id = :id LIMIT 1")
    suspend fun findById(id: Int): RoleEntity?
}
