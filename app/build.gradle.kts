plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler) // only Kotlin 2.0
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kapt)
}

android {
    namespace = "com.example.ksp_sample.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.ksp_sample"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            manifestPlaceholders
        }
        getByName("release") {
            isMinifyEnabled = true
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
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.activity.compose)

    implementation(platform(libs.compose.bom))
    implementation(libs.foundation)
    implementation(libs.ui)
    implementation(libs.material3)
    implementation(libs.runtime)

    implementation(project(":db"))
    implementation(libs.room.ktx)

    implementation(project(":view_model_factory"))
    kapt(project(":view_model_factory"))
}
