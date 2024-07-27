package com.example.ksp_sample.view_model_factory

//import com.google.devtools.ksp.processing.CodeGenerator
//import com.google.devtools.ksp.processing.Dependencies
//import com.google.devtools.ksp.processing.KSPLogger
//import com.google.devtools.ksp.processing.Resolver
//import com.google.devtools.ksp.processing.SymbolProcessor
//import com.google.devtools.ksp.symbol.KSAnnotated
//import com.google.devtools.ksp.symbol.KSClassDeclaration
//import com.google.devtools.ksp.symbol.KSVisitorVoid
//import com.google.devtools.ksp.validate
//import java.io.OutputStream
//
//class ViewModelFactoryProcessor(
//    private val codeGenerator: CodeGenerator,
//    private val logger: KSPLogger
//) : SymbolProcessor {
//
//    override fun process(resolver: Resolver): List<KSAnnotated> {
//        val symbols =
//            resolver.getSymbolsWithAnnotation("com.example.ksp_sample.view_model_factory.ViewModelFactory")
//
//        symbols.forEach { symbol ->
//            logger.info("Symbol annotated with ViewModelFactory annotation", symbol)
//        }
//
//        symbols.filter { symbol ->
//            symbol is KSClassDeclaration && symbol.validate()
//        }.forEach { symbol ->
//            symbol.accept(ViewModelFactoryVisitor(), Unit)
//        }
//
//        return symbols.filter { !it.validate() }.toList()
//    }
//
//    inner class ViewModelFactoryVisitor : KSVisitorVoid() {
//        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
//            val packageName = classDeclaration.containingFile!!.packageName.asString()
//            val viewModelName = classDeclaration.simpleName.asString()
//            val className = "${viewModelName}Factory"
//            val primaryConstructors = classDeclaration.primaryConstructor!!.parameters
//
//            val file = codeGenerator.createNewFile(
//                Dependencies(true, classDeclaration.containingFile!!),
//                packageName,
//                className
//            )
//
//            // package
//            file.appendText("package $packageName\n\n")
//
//            // import
//            file.appendText("import androidx.lifecycle.ViewModel\n")
//            file.appendText("import androidx.lifecycle.ViewModelProvider\n")
//            file.appendText("import androidx.lifecycle.viewmodel.CreationExtras\n")
//            classDeclaration.primaryConstructor!!.parameters.forEach { param ->
//                when (param.type.element.toString()) {
//                    "SavedStateHandle" -> {
//                        file.appendText("import androidx.lifecycle.SavedStateHandle\n")
//                        file.appendText("import androidx.lifecycle.createSavedStateHandle\n")
//                    }
//                    "KspSampleApplication" -> {
//                        file.appendText("import com.example.ksp_sample.KspSampleApplication\n")
//                    }
//                    else -> {}
//                }
//            }
//            file.appendText("import ${packageName}.${viewModelName}\n\n")
//
//            // class
//            var viewModelFactoryConstructor = ""
//            primaryConstructors.forEachIndexed { i, param ->
//                val typeName = param.type.element.toString()
//
//                if (typeName == "SavedStateHandle") return@forEachIndexed
//                if (viewModelFactoryConstructor.isNotBlank()) {
//                    viewModelFactoryConstructor += ", "
//                }
//
//                viewModelFactoryConstructor += "private val param$i: $typeName"
//            }
//            file.appendText("class ${className}($viewModelFactoryConstructor) : ViewModelProvider.Factory {\n\n")
//
//            // function
//            var viewModelConstructors = ""
//            primaryConstructors.forEachIndexed { i, param ->
//                if (viewModelConstructors.isNotBlank()) {
//                    viewModelConstructors += ", "
//                }
//                if (param.type.element.toString() == "SavedStateHandle") {
//                    viewModelConstructors += "extras.createSavedStateHandle()"
//                    return@forEachIndexed
//                }
//
//                viewModelConstructors += "param$i"
//            }
//            file.appendText("    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {\n")
//            file.appendText("        return if (modelClass.isAssignableFrom(${viewModelName}::class.java)) {\n")
//            file.appendText("            @Suppress(\"UNCHECKED_CAST\")\n")
//            file.appendText("            ${viewModelName}($viewModelConstructors) as T\n")
//            file.appendText("        } else {\n")
//            file.appendText("            throw IllegalArgumentException(\"Unknown ViewModel class\")\n")
//            file.appendText("        }\n")
//            file.appendText("    }\n")
//            file.appendText("}\n")
//
//            file.close()
//        }
//
//        private fun OutputStream.appendText(str: String) {
//            this.write(str.toByteArray())
//        }
//    }
//}
