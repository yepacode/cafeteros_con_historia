package com.cafeteros.historia.data.model

import com.cafeteros.historia.data.local.database.entities.UserEntity
import com.cafeteros.historia.ui.features.auth.components.UserType


data class User(
    val id: Long,
    val email: String,
    val name: String,
    val phone: String,
    val userType: UserType
)

/** Convierte una entidad de Room en el modelo de dominio. */
fun UserEntity.toDomain(): User = User(
    id = id,
    email = email,
    name = name,
    phone = phone,
    userType = UserType.fromRoleId(roleId)
)

/**
 * Convierte el modelo de dominio en una entidad lista para insertar/actualizar.
 *
 * @param passwordHash hash SHA-256 ya calculado por el repositorio.
 */
fun User.toEntity(passwordHash: String): UserEntity = UserEntity(
    id = id,
    email = email,
    name = name,
    phone = phone,
    passwordHash = passwordHash,
    roleId = userType.roleId
)
