package com.cafeteros.historia.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Tabla `roles`: catálogo de roles de usuario.
 *
 * Esta tabla se siembra automáticamente al crear la base de datos con dos
 * filas fijas:
 *  - id=1, name="Comprador"
 *  - id=2, name="Vendedor"
 *
 * El `id` es asignado manualmente (no autogenerado) porque actúa como
 * identificador semántico estable para mapear desde el enum
 * [com.cafeteros.historia.ui.features.auth.components.UserType].
 *
 * Si en el futuro se agregan más roles (ej. "Administrador"), basta con
 * insertar una fila adicional aquí — la columna `role_id` en `users`
 * automáticamente acepta el nuevo valor por la FK.
 */
@Entity(tableName = "roles")
data class RoleEntity(
    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "name")
    val name: String
)
