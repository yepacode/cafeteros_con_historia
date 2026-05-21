package com.cafeteros.historia.data.repository

import com.cafeteros.historia.data.model.SavedAddress
import com.cafeteros.historia.data.remote.firestore.AddressesRemoteDataSource
import kotlinx.coroutines.flow.Flow

/**
 * Punto único de acceso a la libreta de direcciones del comprador.
 */
class AddressRepository(
    private val remote: AddressesRemoteDataSource
) {
    suspend fun saveAddress(uid: String, address: SavedAddress): String =
        remote.upsert(uid, address)

    suspend fun deleteAddress(uid: String, addressId: String) =
        remote.delete(uid, addressId)

    suspend fun setDefault(uid: String, addressId: String) =
        remote.setDefault(uid, addressId)

    fun observeMyAddresses(uid: String): Flow<List<SavedAddress>> = remote.observe(uid)
}
