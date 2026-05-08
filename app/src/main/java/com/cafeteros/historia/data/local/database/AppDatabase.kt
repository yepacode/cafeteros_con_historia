package com.cafeteros.historia.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.cafeteros.historia.data.local.database.dao.RoleDao
import com.cafeteros.historia.data.local.database.dao.UserDao
import com.cafeteros.historia.data.local.database.entities.RoleEntity
import com.cafeteros.historia.data.local.database.entities.UserEntity

/** Nombre del archivo SQLite donde Room persiste todos los datos. */
private const val DATABASE_NAME = "cafeteros.db"

/**
 * Base de datos Room de la app.
 *
 * Versión 2 introduce la tabla `roles` y reemplaza la columna `user_type`
 * (TEXT) en la tabla `users` por `role_id` (INTEGER FK → roles.id).
 *
 * Cuando se agreguen nuevas entidades (productos, pedidos, etc.):
 *  1. Crear el `@Entity` correspondiente.
 *  2. Crear su DAO.
 *  3. Añadirlo al array `entities` y aumentar [version].
 *  4. Definir una `Migration` (o seguir con `fallbackToDestructiveMigration`
 *     mientras se está en desarrollo).
 */
@Database(
    entities = [UserEntity::class, RoleEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun roleDao(): RoleDao

    companion object {

        /**
         * Construye la instancia singleton de la base de datos.
         *
         * Llamar UNA sola vez desde el `ServiceLocator`. No invocar desde
         * Activities ni ViewModels — usar la instancia compartida.
         *
         * Al crearse por primera vez, [SeedRolesCallback] inserta los dos
         * roles fijos (Comprador y Vendedor) para que `users.role_id` siempre
         * pueda resolver su FK.
         */
        fun build(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME
            )
                // En desarrollo: si el esquema cambia, se borra la DB en vez
                // de pedir migración. Quitar antes de lanzar a usuarios reales.
                .fallbackToDestructiveMigration(dropAllTables = true)
                .addCallback(SeedRolesCallback)
                .build()
        }
    }
}

/**
 * Callback de Room que siembra la tabla `roles` con sus dos filas fijas
 * (1=Comprador, 2=Vendedor).
 *
 * Se sobrescriben tres puntos para garantizar la siembra en TODOS los casos:
 *  - [onCreate]: instalación nueva (no existe el archivo .db).
 *  - [onDestructiveMigration]: cambio de versión con `fallbackToDestructiveMigration`
 *    (Room recrea las tablas pero NO vuelve a llamar `onCreate`).
 *  - [onOpen]: red de seguridad — cada vez que se abre la DB, garantiza que
 *    los roles base existan, sin importar cómo se llegó hasta aquí.
 *
 * Las inserciones usan `INSERT OR IGNORE` para que sean idempotentes: si los
 * roles ya existen, no falla por duplicados.
 */
private object SeedRolesCallback : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        seedRoles(db)
    }

    override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
        super.onDestructiveMigration(db)
        seedRoles(db)
    }

    override fun onOpen(db: SupportSQLiteDatabase) {
        super.onOpen(db)
        seedRoles(db)
    }

    private fun seedRoles(db: SupportSQLiteDatabase) {
        db.execSQL("INSERT OR IGNORE INTO roles (id, name) VALUES (1, 'Comprador')")
        db.execSQL("INSERT OR IGNORE INTO roles (id, name) VALUES (2, 'Vendedor')")
    }
}
