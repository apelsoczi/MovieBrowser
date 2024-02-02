@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.mbh.moviebrowser"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mbh.moviebrowser"
        minSdk = 26
        //noinspection EditedTargetSdkVersion
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
         kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":api"))
    // region: core
    implementation(libs.core.kotlin)
    implementation(libs.core.ktx)
    implementation(libs.kotlin.coroutines)
    // endregion
    // region: compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.activity)
    implementation(libs.compose.navigation)
    implementation(libs.compose.preview)
    implementation(libs.compose.tooling)
    implementation(libs.compose.viewmodel)
    implementation(libs.compose.material)
    implementation(libs.compose.lifecycle)
    implementation(libs.compose.lifecycle.ktx)
    implementation(libs.ui.coil)
    implementation(libs.ui.coilCompose)
    implementation(libs.hilt.navigation.compose)
    // endregion
    // region: navigation
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.navigation.destinations)
    ksp(libs.navigation.destinations.ksp)
    // endregion
    // region: DI
    implementation(libs.hilt)
    kapt(libs.hilt.compiler)
    // endregion
    // region: data
    implementation(libs.kotlin.serialization)
    // region: test
    testImplementation(libs.test.junit)
    testImplementation(libs.test.mockk)
    testImplementation(libs.test.truth)
    testImplementation(libs.test.coroutines)
    testImplementation(libs.test.turbine)
    testImplementation(libs.test.okhttp.mockwebserver)
    // endregion
}
