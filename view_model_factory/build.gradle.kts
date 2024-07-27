plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

dependencies {
    // implementation(libs.symbol.processing.api)

    // only KSP2
    // testImplementation(libs.junit)
    // testImplementation(libs.symbol.processing.aa.embeddable)
    // testImplementation(libs.symbol.processing.common.deps)
}

sourceSets.main {
    java.srcDirs("src/main/kotlin")
}
