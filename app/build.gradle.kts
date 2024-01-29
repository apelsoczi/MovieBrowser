@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application)
    kotlin("android")
    kotlin("kapt")
}

android {
    namespace = "com.mbh.moviebrowser"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mbh.moviebrowser"
        minSdk = 24
        //noinspection EditedTargetSdkVersion
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "API_KEY", "\"3026a59c471f4497650a2fa46a1555cd\"")

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
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

dependencies {
    // region: core
    implementation(libs.core.kotlin)
    implementation(libs.core.ktx)
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
    // endregion
    // region: navigation
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    // endregion
    // region: data
    implementation(libs.retrofit)
    implementation(libs.retrofitGson)
    implementation(libs.data.dataStore)
    implementation(libs.data.room)
    implementation(libs.data.room.ktx)
    kapt(libs.data.room.compiler)
    implementation(libs.okhttp.logger)
    // endregion
    // region: coroutines
    implementation(libs.kotlin.coroutines)
    // endregion
    // region: DI
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)
    // endregion
    // region: utils
    implementation(libs.gson)
    // endregion
}
