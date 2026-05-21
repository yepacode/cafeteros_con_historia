package com.cafeteros.historia

import android.app.Application
import com.cafeteros.historia.data.local.preferences.EncryptedCredentialsStore
import com.cafeteros.historia.data.local.preferences.SessionDataStore
import com.cafeteros.historia.data.remote.auth.FirebaseAuthDataSource
import com.cafeteros.historia.data.remote.firestore.AddressesRemoteDataSource
import com.cafeteros.historia.data.remote.firestore.ConversationsRemoteDataSource
import com.cafeteros.historia.data.remote.firestore.FarmsRemoteDataSource
import com.cafeteros.historia.data.remote.firestore.NotificationsRemoteDataSource
import com.cafeteros.historia.data.remote.firestore.OrdersRemoteDataSource
import com.cafeteros.historia.data.remote.firestore.ProductsRemoteDataSource
import com.cafeteros.historia.data.remote.firestore.ReviewsRemoteDataSource
import com.cafeteros.historia.data.remote.firestore.UsersRemoteDataSource
import com.cafeteros.historia.data.repository.AddressRepository
import com.cafeteros.historia.data.repository.ConversationRepository
import com.cafeteros.historia.data.repository.FarmRepository
import com.cafeteros.historia.data.repository.NotificationRepository
import com.cafeteros.historia.data.repository.OrderRepository
import com.cafeteros.historia.data.repository.ProductRepository
import com.cafeteros.historia.data.repository.ReviewRepository
import com.cafeteros.historia.data.repository.UserRepository

/**
 * Application class de Cafeteros.
 *
 * Construye las dependencias singleton de la app la primera vez que se
 * necesitan y las expone vía propiedades `lazy`. Es el "service locator"
 * manual del proyecto — más que suficiente para el alcance actual sin
 * agregar Hilt u otra librería de inyección.
 *
 * Firebase ([com.google.firebase.FirebaseApp]) se inicializa automáticamente
 * por el plugin `google-services` cuando arranca la app, así que aquí no
 * necesitamos llamar a `FirebaseApp.initializeApp` manualmente.
 *
 * Acceso típico desde una Activity:
 * ```
 * val repo = (application as CafeterosApplication).userRepository
 * ```
 */
class CafeterosApplication : Application() {

    // ── Banderas locales del dispositivo ──────────────────────────
    private val sessionDataStore: SessionDataStore by lazy { SessionDataStore(this) }

    // Bóveda cifrada con AES-256 para credenciales del usuario (login huella).
    private val credentialsStore: EncryptedCredentialsStore by lazy {
        EncryptedCredentialsStore(this)
    }

    // ── Acceso a Firebase Auth y Firestore ────────────────────────
    private val firebaseAuthDataSource: FirebaseAuthDataSource by lazy {
        FirebaseAuthDataSource()
    }
    private val usersRemoteDataSource: UsersRemoteDataSource by lazy {
        UsersRemoteDataSource()
    }

    /** Único repositorio de usuario expuesto a la capa de UI. */
    val userRepository: UserRepository by lazy {
        UserRepository(
            authDataSource = firebaseAuthDataSource,
            usersRemote = usersRemoteDataSource,
            sessionDataStore = sessionDataStore,
            credentialsStore = credentialsStore
        )
    }

    // ── Productos del caficultor (Firestore + imagen Base64) ──────
    private val productsRemoteDataSource: ProductsRemoteDataSource by lazy {
        ProductsRemoteDataSource()
    }

    /**
     * Repositorio de productos. Necesita el applicationContext para que
     * el helper de compresión pueda abrir las Uris de cámara/galería.
     */
    val productRepository: ProductRepository by lazy {
        ProductRepository(
            productsRemote = productsRemoteDataSource,
            appContext = applicationContext
        )
    }

    // ── Perfil de la finca del caficultor ─────────────────────────
    private val farmsRemoteDataSource: FarmsRemoteDataSource by lazy {
        FarmsRemoteDataSource()
    }

    /** Repositorio de la finca. Comparte el contexto para compresión de foto. */
    val farmRepository: FarmRepository by lazy {
        FarmRepository(
            farmsRemote = farmsRemoteDataSource,
            appContext = applicationContext
        )
    }

    // ── Pedidos (colección /orders) ─────────────────────────────
    private val ordersRemoteDataSource: OrdersRemoteDataSource by lazy {
        OrdersRemoteDataSource()
    }

    /** Repositorio de pedidos para ambos roles (comprador y caficultor). */
    val orderRepository: OrderRepository by lazy {
        OrderRepository(ordersRemote = ordersRemoteDataSource)
    }

    // ── Reseñas (colección /reviews) ─────────────────────────────
    private val reviewsRemoteDataSource: ReviewsRemoteDataSource by lazy {
        ReviewsRemoteDataSource()
    }

    /**
     * Repositorio de reseñas. El comprador crea reseñas tras DELIVERED;
     * el caficultor las consume desde su pantalla de Reseñas.
     */
    val reviewRepository: ReviewRepository by lazy {
        ReviewRepository(reviewsRemote = reviewsRemoteDataSource)
    }

    // ── Notificaciones (colección /notifications) ────────────────
    private val notificationsRemoteDataSource: NotificationsRemoteDataSource by lazy {
        NotificationsRemoteDataSource()
    }

    /**
     * Repositorio de notificaciones locales. Se dispara como efecto
     * secundario de eventos (nuevo pedido, cambio de estado, reseña) y
     * el destinatario las consume en su pantalla de notificaciones.
     */
    val notificationRepository: NotificationRepository by lazy {
        NotificationRepository(remote = notificationsRemoteDataSource)
    }

    // ── Mensajería (chat 1-a-1) ─────────────────────────────────
    private val conversationsRemoteDataSource: ConversationsRemoteDataSource by lazy {
        ConversationsRemoteDataSource()
    }

    /**
     * Repositorio de conversaciones y mensajes. Soporta chat 1-a-1
     * entre comprador y caficultor; el id de la conversación es
     * determinístico (uids ordenados) para que cualquier parte la
     * pueda resolver sin queries.
     */
    val conversationRepository: ConversationRepository by lazy {
        ConversationRepository(remote = conversationsRemoteDataSource)
    }

    // ── Direcciones del comprador (/users/{uid}/addresses) ──────
    private val addressesRemoteDataSource: AddressesRemoteDataSource by lazy {
        AddressesRemoteDataSource()
    }

    /**
     * Repositorio de la libreta de direcciones del comprador. Se usa
     * tanto en [com.cafeteros.historia.ui.features.addressbook] como
     * dentro del checkout para preseleccionar la dirección default.
     */
    val addressRepository: AddressRepository by lazy {
        AddressRepository(remote = addressesRemoteDataSource)
    }

    /**
     * Acceso público al [SessionDataStore] para que la UI lea/escriba flags
     * locales (biometría habilitada, último email) sin pasar por el repo.
     */
    val sessionPreferences: SessionDataStore get() = sessionDataStore
}
