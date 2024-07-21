package com.example.ksp_sample.view_model_factory

import com.google.devtools.ksp.impl.KotlinSymbolProcessing
import com.google.devtools.ksp.processing.KSPJvmConfig
import com.google.devtools.ksp.processing.KspGradleLogger
import ksp.org.jetbrains.kotlin.config.JvmTarget
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class ViewModelFactoryProcessorTest {
    @JvmField
    @Rule
    val dummyFolder = TemporaryFolder()

    @Test
    fun generate_ViewModelFactory_Test() {
        val viewModelFactoryFolder = dummyFolder.newFolder("app/src/main/kotlin/com/example/ksp_sample/view_model_factory")
        val viewModel = File(viewModelFactoryFolder, "TestViewModel.kt")
        viewModel.writeText("""
            package com.example.ksp_sample.view_model_factory

            @ViewModelFactory
            class TestViewModel

            annotation class ViewModelFactory
        """.trimIndent())

        val mainOutputDir = dummyFolder.newFolder("app/build/generated/ksp/main")

        val kotlinOutputFolder = dummyFolder.newFolder("app/build/generated/ksp/main/kotlin")
        val viewModelFactoryOutputFolder = dummyFolder.newFolder("app/build/generated/ksp/main/kotlin/com/example/ksp_sample/view_model_factory")
        val generatedFile = File(viewModelFactoryOutputFolder, "TestViewModelFactory.kt")

        val kspConfig = KSPJvmConfig.Builder().apply {
            javaOutputDir = dummyFolder.newFolder("app/build/generated/ksp/main/java")
            jvmTarget = JvmTarget.JVM_17.description

            moduleName = "app"
            sourceRoots = listOf(viewModel)

            projectBaseDir = dummyFolder.root
            outputBaseDir = mainOutputDir
            cachesDir = dummyFolder.newFolder("app/build/generated/ksp/main/caches")

            classOutputDir = dummyFolder.newFolder("app/build/generated/ksp/main/classes")
            kotlinOutputDir = kotlinOutputFolder
            resourceOutputDir = dummyFolder.newFolder("app/build/generated/ksp/main/resources")

            languageVersion = "2.0.0"
            apiVersion = "2.0.0-1.0.23"
        }.build()

        val logger = KspGradleLogger(loglevel = KspGradleLogger.LOGGING_LEVEL_INFO)

        val exitCode = KotlinSymbolProcessing(
            kspConfig = kspConfig,
            symbolProcessorProviders = listOf(ViewModelFactoryProcessorProvider()),
            logger = logger
        ).execute()

        assertTrue(exitCode == KotlinSymbolProcessing.ExitCode.OK)
        assertTrue(generatedFile.exists())
    }
}
