plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

dependencies {
    implementation(libs.symbol.processing.api)
}

sourceSets.main {
    java.srcDirs("src/main/kotlin")
}
