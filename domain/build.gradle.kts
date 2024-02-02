@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.mbh.moviebrowser.domain"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(project(":api"))
    // region: core
    implementation(libs.core.kotlin)
    implementation(libs.core.ktx)
    implementation(libs.kotlin.coroutines)
    // endregion
    // region data
    implementation(libs.data.dataStore)
    implementation(libs.data.room)
    implementation(libs.data.room.ktx)
    kapt(libs.data.room.compiler)
    implementation(libs.kotlin.serialization)
    implementation(libs.gson)
    // endregion
    // region: DI
    implementation(libs.hilt)
    kapt(libs.hilt.compiler)
    // endregion
    // region: test
    testImplementation(libs.test.junit)
    testImplementation(libs.test.mockk)
    testImplementation(libs.test.truth)
    testImplementation(libs.test.coroutines)
    testImplementation(libs.test.turbine)
    androidTestImplementation(libs.test.junit)
    androidTestImplementation(libs.test.espresso)
    androidTestImplementation(libs.test.truth)
    // endregion
}