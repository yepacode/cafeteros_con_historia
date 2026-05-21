import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    // KSP ya no se usa (era para el compilador de Room). Si lo necesitas
    // de nuevo, vuelve a agregar `alias(libs.plugins.ksp)`.
    // Activa la generación de recursos a partir de google-services.json.
    alias(libs.plugins.google.services)
}

// Carga credenciales de firma desde keystore.properties (excluido de git).
// Si el archivo no existe, el build release sigue funcionando pero sin firmar.
val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties().apply {
    if (keystorePropertiesFile.exists()) {
        load(FileInputStream(keystorePropertiesFile))
    }
}

android {
    namespace = "com.cafeteros.historia"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.cafeteros.historia"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            if (keystorePropertiesFile.exists()) {
                storeFile = rootProject.file(keystoreProperties["storeFile"] as String)
                storePassword = keystoreProperties["storePassword"] as String
                keyAlias = keystoreProperties["keyAlias"] as String
                keyPassword = keystoreProperties["keyPassword"] as String
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // Aplica la firma solo si keystore.properties existe.
            if (keystorePropertiesFile.exists()) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    implementation(libs.androidx.activity)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.biometric)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // ── Persistencia local ──────────────────────────────────────
    // Room se removió: Firestore es la única fuente. DataStore sigue
    // usándose para banderas locales (biometría habilitada, último email).
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.security.crypto)
    implementation(libs.coil.compose)

    // ── Firebase (Auth + Firestore) ─────────────────────────────
    // El BoM mantiene compatibilidad entre versiones de SDKs hijos.
    // Storage NO se incluye: requiere plan Blaze (de pago). En su lugar,
    // las imágenes se guardan como Base64 dentro del documento de Firestore.
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)

    // ── Geolocalización: GPS + mapa OSM (sin Google Maps) ───────
    implementation(libs.play.services.location)
    implementation(libs.osmdroid.android)

    // ── Coroutines + Play Services (Task.await()) ───────────────
    implementation(libs.kotlinx.coroutines.play.services)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
