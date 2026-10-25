
plugins {
    // Configura la aplicación Android.
    alias(libs.plugins.android.application)

    // Permite utilizar Kotlin con Jetpack Compose.
    alias(libs.plugins.kotlin.compose)

    // Permite generar el código necesario para Room.
    alias(libs.plugins.ksp)
}

android {
    namespace = "cl.duoc.esfocar"

    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "cl.duoc.esfocar"
        minSdk = 26
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = true
                packageScope = setOf(
                    "androidx.**",
                    "kotlin.**",
                    "kotlinx.**"
                )
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
    // Dependencias originales de Jetpack Compose.
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Room: persistencia local mediante SQLite.
    implementation(libs.androidx.room.runtime)

    // Integración de Room con Kotlin y corrutinas.
    implementation(libs.androidx.room.ktx)

    // Genera el código que Room necesita durante la compilación.
    ksp(libs.androidx.room.compiler)

    // Dependencias de pruebas originales.
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)

    // Herramientas de depuración.
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
