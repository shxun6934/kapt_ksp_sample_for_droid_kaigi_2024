// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // android
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false

    // kotlin
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false

    // hilt
    alias(libs.plugins.hilt) apply false

    // kapt
    alias(libs.plugins.kapt) apply false

    // ksp
    alias(libs.plugins.ksp) apply false
}
