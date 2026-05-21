# Cafeteros — Marketplace nativo Android de café colombiano de origen

Aplicación Android nativa, escrita 100% en **Kotlin con Jetpack Compose**, que conecta directamente caficultores colombianos con compradores finales. Tres roles diferenciados: **Administrador**, **Vendedor (Caficultor)** y **Comprador**.

## Stack tecnológico

| Capa | Tecnología | Versión |
|---|---|---|
| Lenguaje | Kotlin | 2.0.21 |
| UI | Jetpack Compose (Material 3) | BOM 2024.10.01 |
| Persistencia | Room | 2.7.0 |
| Sesión | DataStore Preferences | 1.1.1 |
| Imágenes | Coil Compose | 2.7.0 |
| Biometría | androidx.biometric | 1.1.0 |
| Build | AGP / Gradle | 9.1.1 / 9.3.1 |
| Procesador | KSP | 2.0.21-1.0.27 |
| API target | minSdk / targetSdk / compileSdk | 30 / 36 / 36 |

Inyección de dependencias: **Service Locator manual** en `CafeterosApplication`. Sin Hilt/Dagger/Koin (decisión consciente para fase actual; migración planificada a Hilt cuando crezca el grafo).

## Estructura de paquetes

```
com.cafeteros.historia
├── CafeterosApplication.kt         # Service Locator (DB, repos, sesión)
├── MainActivity.kt                 # Router por rol
│
├── data/
│   ├── local/
│   │   ├── database/               # Room: AppDatabase, DAOs, Entities
│   │   └── preferences/            # DataStore: SessionDataStore
│   ├── model/                      # Modelos de dominio
│   ├── repository/                 # UserRepository
│   └── security/                   # PasswordHasher (SHA-256)
│
├── auth/
│   └── BiometricAuthenticator.kt   # Wrapper de BiometricPrompt
│
└── ui/
    ├── theme/                      # BrandColors, BrandTypography, BrandSpacing
    │
    └── features/                   # Feature-based + MVVM
        ├── splash/
        ├── onboarding/
        ├── auth/                   # Login, Register, RecuperarContraseña
        ├── home/                   # FarmerHome (caficultor)
        │
        ├── # Comprador
        ├── explore/                # Home del comprador
        ├── coffeemap/              # Mapa cafetero (5 regiones)
        ├── zonedetail/
        ├── caficultordetail/       # Card del vendedor
        ├── caficultorshop/
        ├── productdetail/
        ├── search/ + searchresults/ + filters/
        ├── cart/
        ├── addressbook/
        ├── checkoutshipping/
        ├── checkoutpayment/        # MockPaymentGateway
        ├── orderreview/
        ├── paymentsuccess/ + paymentfailed/
        │
        ├── # Caficultor (Vendedor)
        ├── farmer_onboarding/
        ├── farmer_registration/    # Wizard 5 pasos
        ├── farmer_products/        # CRUD productos (wizard 3 pasos)
        ├── farmer_inventory/
        ├── farmer_sales/
        ├── farmer_wallet/
        ├── farmer_messages/
        ├── farmer_reviews/
        ├── farmer_profile/
        ├── farmer_notifications/
        ├── farmer_stats/
        ├── farmer_help/
        │
        └── # Administrador
            └── admin/              # Dashboard, gestión usuarios/productos, reportes
```

## Roles

| Rol | `roles.id` | Pantalla principal | Capacidades |
|---|---|---|---|
| Administrador | 3 | `AdminDashboardActivity` | KPIs globales, CRUD de usuarios, gestión de productos, reporte de ventas |
| Vendedor (Caficultor) | 2 | `FarmerHomeScreen` | CRUD de productos, inventario, gestión de ventas, billetera, mensajes, reseñas |
| Comprador | 1 | `ExploreActivity` | Catálogo, mapa cafetero, carrito, checkout, perfil |

## Pantallas transversales

- **Splash** (`SplashActivity`) — branding `Origen` con tipografía Serif 48sp.
- **Onboarding** (`OnboardingActivity`) — `HorizontalPager` educativo.
- **Login / Register** (`auth/`) — auth con SHA-256 + biometría.
- **Recuperación de contraseña** (`password_recovery/`) — flujo de 3 sub-pantallas (email → OTP → nueva clave).
- **Drawer / Bottom Navigation** — disponibles en cada home por rol.

## Diseño visual: tokens centralizados

Toda la paleta, tipografía y espaciado vive en Kotlin como evolución moderna del enfoque `values/`:

- [`BrandColors.kt`](app/src/main/java/com/cafeteros/historia/ui/theme/BrandColors.kt) — 100+ colores semánticos. Marca: `CoffeeBrown #3E2723`, `CreamWhite #E8DCC4`, `ForestGreen #1F4D38`, `RatingStar #E3A82B`.
- [`BrandTypography.kt`](app/src/main/java/com/cafeteros/historia/ui/theme/BrandTypography.kt) — Serif para títulos, SansSerif para UI.
- [`BrandSpacing.kt`](app/src/main/java/com/cafeteros/historia/ui/theme/BrandSpacing.kt) — escala 4 / 8 / 16 / 24 / 48 dp.

XML obligatorio en `res/values/`: `themes.xml` (Material3 DayNight), `strings.xml`, drawables vectoriales (`ic_google_g.xml`), adaptive icons en `mipmap-anydpi-v26/`.

## Base de datos

SQLite vía Room (archivo `cafeteros.db`):

| Tabla | Columnas clave | Notas |
|---|---|---|
| `users` | `id`, `email` (UNIQUE), `name`, `phone`, `password_hash`, `role_id` (FK), `created_at` | SHA-256 sin salt; plan: BCrypt |
| `roles` | `id`, `name` | Sembrada con `(1, Comprador)`, `(2, Vendedor)`, `(3, Administrador)` |

`AppDatabase` usa `fallbackToDestructiveMigration` mientras dure el desarrollo. `SeedRolesCallback` garantiza filas base con `INSERT OR IGNORE` en `onCreate`, `onDestructiveMigration` y `onOpen`.

## Cómo compilar

Requisitos: **Android Studio Ladybug** (o superior) y **JDK 11**.

```powershell
# Clonar y entrar al directorio
git clone <URL_REPO>
cd Cafeteros

# Compilar APK debug
./gradlew assembleDebug

# Generar APK release firmado
./gradlew assembleRelease

# Correr unit tests
./gradlew test

# Correr instrumented tests (requiere emulador o dispositivo conectado)
./gradlew connectedAndroidTest
```

El APK debug queda en `app/build/outputs/apk/debug/app-debug.apk`.

## Cuentas demo (seed)

Para probar los tres roles rápido:

| Rol | Email | Contraseña |
|---|---|---|
| Administrador | `admin@cafeteros.co` | `admin123` |
| Caficultor | `vendedor@cafeteros.co` | `vendedor123` |
| Comprador | `comprador@cafeteros.co` | `comprador123` |

> Si la BD está vacía, regístralas desde `RegisterScreen`. La selección de rol determina la fila en `roles` que se asigna como FK.

## Patrones arquitectónicos clave

- **Feature-based + MVVM**: cada feature en su carpeta con `Activity`, `Screen` (Compose), `ViewModel`, `model/`, `components/`.
- **State-driven UI**: cada pantalla observa un `StateFlow<UiState>` del ViewModel; recomposición reactiva.
- **Sealed classes para resultados**: `LoginResult`, `RegisterResult`, etc., para `when` exhaustivo.
- **Mapper pattern**: extensiones `UserEntity.toDomain()` ↔ `User.toEntity()` para separar persistencia y dominio.
- **Navegación por Intents**: cada feature en su Activity, con companion `start()` para encapsular extras tipados.

## Roadmap

- [ ] Backend real (Retrofit + ktor) reemplazando `MockPaymentGateway` y `ProductsStore` in-memory.
- [ ] Persistencia de productos en Room.
- [ ] Migración de hash a BCrypt + salt por usuario.
- [ ] Migración a Hilt para inyección de dependencias.
- [ ] Navigation Compose unificado en lugar de Activities sueltas.
- [ ] Tests unitarios e integración (JUnit5 + MockK + Espresso).
- [ ] i18n (es/en).
- [ ] Layouts adaptativos para tablet (`sw600dp`).

## Convenciones de commits

```
feat(<scope>): nueva funcionalidad
fix(<scope>): corrección de bug
docs(<scope>): documentación
refactor(<scope>): reestructuración sin cambio de comportamiento
chore(<scope>): tareas auxiliares (build, deps, etc.)
test(<scope>): tests
```

Ejemplos:
- `feat(admin): add user management screen with CRUD operations`
- `fix(farmer-registration): replace deprecated FlowRow with custom Row chunked`
- `docs: add README with architecture and setup instructions`

## Licencia

Proyecto académico. Todos los derechos reservados a los autores.
