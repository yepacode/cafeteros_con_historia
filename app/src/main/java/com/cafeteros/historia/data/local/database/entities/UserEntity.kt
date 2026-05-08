package com.cafeteros.historia.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Representación Room de la tabla `users`.
 *
 * Esta clase es **únicamente** la capa de persistencia: no debe usarse en
 * la UI ni en los ViewModels directamente. Para eso existe el modelo de
 * dominio [com.cafeteros.historia.data.model.User], que se obtiene mediante
 * [com.cafeteros.historia.data.model.toDomain] / [com.cafeteros.historia.data.model.toEntity].
 *
 * Diseño normalizado:
 *  - `email` tiene índice único → no se permiten dos cuentas con el mismo correo.
 *  - `role_id` es una FK hacia [RoleEntity.id] → solo se aceptan roles válidos
 *    y la integridad referencial se aplica a nivel SQLite. Si se intenta borrar
 *    un rol que está en uso, [ForeignKey.RESTRICT] lo bloquea.
 *  - `role_id` tiene índice también, recomendado por Room para FKs (acelera joins).
 */
@Entity(
    tableName = "users",
    indices = [Index(value = ["email"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = RoleEntity::class,
            parentColumns = ["id"],
            childColumns = ["role_id"],
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "phone")
    val phone: String,

    /** Hash SHA-256 hex de la contraseña; nunca se almacena en texto plano. */
    @ColumnInfo(name = "password_hash")
    val passwordHash: String,

    /** FK al id de la tabla `roles` (1=Comprador, 2=Vendedor). */
    @ColumnInfo(name = "role_id", index = true)
    val roleId: Int,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
