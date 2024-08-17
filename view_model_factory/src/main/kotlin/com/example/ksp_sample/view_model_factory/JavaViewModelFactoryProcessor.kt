package com.example.ksp_sample.view_model_factory

import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.PackageElement
import javax.lang.model.element.TypeElement

class JavaViewModelFactoryProcessor : AbstractProcessor() {

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf("com.example.ksp_sample.view_model_factory.ViewModelFactory")
    }

    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {
        val elements = roundEnv!!.getElementsAnnotatedWith(ViewModelFactory::class.java)

        elements.filterIsInstance<TypeElement>().map { typeElement ->
            val packageName = (typeElement.enclosingElement as PackageElement).qualifiedName.toString()
            val viewModelName = typeElement.simpleName.toString()
            val className = "${viewModelName}Factory"
            val isSavedStateHandle = typeElement.enclosedElements.filterIsInstance<ExecutableElement>()
                .first { it.simpleName.toString() == "<init>" }
                .parameters.any { it.asType().toString() == "androidx.lifecycle.SavedStateHandle" }

            val file = processingEnv.filer.createSourceFile("${packageName}.${className}")

            file.openWriter().use { writer ->
                writer.apply {
                    // package
                    write("package ${packageName};\n\n")

                    // import
                    if (isSavedStateHandle) {
                        write("import android.os.Bundle;\n\n")

                        write("import androidx.annotation.Nullable;\n")
                        write("import androidx.lifecycle.SavedStateHandle;\n")
                        write("import androidx.savedstate.SavedStateRegistryOwner;\n")
                    }
                    write("import androidx.annotation.NonNull;\n")
                    write("import androidx.lifecycle.ViewModel;\n")
                    val viewModelProvider = if (isSavedStateHandle) {
                        "AbstractSavedStateViewModelFactory"
                    } else {
                        "ViewModelProvider"
                    }
                    write("import androidx.lifecycle.$viewModelProvider;\n\n")

                    write("import com.example.ksp_sample.KspSampleApplication;\n\n")

                    // class
                    val extend = if (isSavedStateHandle) {
                        "extends AbstractSavedStateViewModelFactory"
                    } else {
                        "implements ViewModelProvider.Factory"
                    }
                    write("public class $className $extend {\n")
                    write("    private final KspSampleApplication app;\n\n")

                    // constructor
                    val constructorArguments = if (isSavedStateHandle) {
                        "@NonNull KspSampleApplication app, @NonNull SavedStateRegistryOwner owner, @Nullable Bundle defaultArgs"
                    } else {
                        "@NonNull KspSampleApplication app"
                    }
                    write("    public ${className}($constructorArguments) {\n")
                    if (isSavedStateHandle) {
                        write("        super(owner, defaultArgs);\n")
                    }
                    write("        this.app = app;\n")
                    write("    }\n\n")

                    // function
                    val modifier = if (isSavedStateHandle) {
                        "protected"
                    } else {
                        "public"
                    }
                    val functionArguments = if (isSavedStateHandle) {
                        "@NonNull String key, @NonNull Class<T> modelClass, @NonNull SavedStateHandle handle"
                    } else {
                        "@NonNull Class<T> modelClass"
                    }
                    val viewModelConstructors = if (isSavedStateHandle) {
                        "handle, app"
                    } else {
                        "app"
                    }
                    write("    @NonNull\n")
                    write("    @Override\n")
                    write("    $modifier <T extends ViewModel> T create($functionArguments) {\n")
                    write("        if (modelClass.isAssignableFrom(${viewModelName}.class)) {\n")
                    write("            return (T) new ${viewModelName}($viewModelConstructors);\n")
                    write("        }\n")
                    write("        throw new IllegalArgumentException(\"Unknown ViewModel class\");\n")
                    write("    }\n")
                    write("}\n")
                }
            }
        }
        return true
    }
}
