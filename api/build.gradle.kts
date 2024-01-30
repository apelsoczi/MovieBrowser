@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.mbh.moviebrowser.api"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        buildConfigField("String", "API_KEY", "\"3026a59c471f4497650a2fa46a1555cd\"")

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
    // region: network
    implementation(libs.okhttp)
    implementation(libs.okhttp.logger)
    implementation(libs.retrofit)
    implementation(libs.retrofitGson)
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
    testImplementation(libs.test.okhttp.mockwebserver)
    // endregion
}