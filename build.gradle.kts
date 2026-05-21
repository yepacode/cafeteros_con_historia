// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
    // Plugin de Firebase: lee `google-services.json` y genera los recursos
    // necesarios para que el SDK encuentre el projectId, apiKey, etc.
    alias(libs.plugins.google.services) apply false
}
