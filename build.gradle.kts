
// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    // Plugin para compilar aplicaciones Android.
    alias(libs.plugins.android.application) apply false

    // Plugin para utilizar Kotlin con Jetpack Compose.
    alias(libs.plugins.kotlin.compose) apply false

    // Plugin que Room utilizará para generar código mediante KSP.
    alias(libs.plugins.ksp) apply false
}
