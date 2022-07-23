package com.ktan.processor

import com.google.devtools.ksp.*
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.visitor.KSValidateVisitor
import com.ktan.annotations.Extras
import com.ktan.annotations.Mutable
import com.ktan.annotations.Required
import com.ktan.annotations.Route
import java.io.OutputStream

class KtanProcessor(environment: SymbolProcessorEnvironment) : SymbolProcessor {

    private val logger = environment.logger

    private val codeGenerator = environment.codeGenerator

    override fun process(resolver: Resolver): List<KSAnnotated> {
        logger.info("Start Ktan Processor")

        val dependencies = Dependencies(false, *resolver.getAllFiles().toList().toTypedArray())
        val extrasSymbols = resolver
            .getSymbolsWithAnnotation(Extras::class.java.name)
        val routeSymbols = resolver
            .getSymbolsWithAnnotation(Route::class.java.name)
        val extrasSymbolsToProcess =
            extrasSymbols.filter { it is KSClassDeclaration && it.validate() }
        val routeSymbolsToProcess =
            routeSymbols.filter {
                it is KSClassDeclaration && it.accept(KtanKSValidateVisitor(), null)
            }
        val unableToProcess =
            extrasSymbols.filterNot { it.validate() }.plus(routeSymbols.filterNot {
                it.accept(
                    KtanKSValidateVisitor(), null
                )
            })

        extrasSymbolsToProcess
            .forEach { it.accept(ExtrasKClassVisitor(dependencies), Unit) }
        routeSymbolsToProcess
            .forEach { it.accept(RouteKClassVisitor(dependencies), Unit) }

        return unableToProcess.toList()
    }

    private inner class ExtrasKClassVisitor(val dependencies: Dependencies) : KSVisitorVoid() {

        override fun visitClassDeclaration(
            classDeclaration:
            KSClassDeclaration, data: Unit
        ) {
            if (classDeclaration.isAbstract()) {
                logger.error(
                    "||Class Annotated with Extras should not abstract", classDeclaration
                )
            }

            if (classDeclaration.isOpen()) {
                logger.error(
                    "||Class Annotated with Projections should kotlin data class", classDeclaration
                )
            }

            val className = classDeclaration.simpleName.getShortName()
            val packageName = classDeclaration.packageName.asString()
            val classPackage = "$packageName.$className"
            val properties = classDeclaration.getDeclaredProperties()
            val bindingProperties = StringBuilder()
            val routerProperties = StringBuilder()
            val fileNameGenerated = "${className}Ktan"

            logger.info("package $classPackage", classDeclaration)

            properties.filter { it.validate() }.forEach {
                var declaringProperty = "val"
                var delegatedProperty = "extraOf(extras.${it.simpleName.getShortName()})"

                logger.info(
                    "${it.simpleName.getShortName()} found in $className with type ${it.type}"
                )

                if (it.annotations.isAnnotationPresent(Mutable::class.java.simpleName)) {
                    declaringProperty = "var"
                }
                if (it.annotations.isAnnotationPresent(Required::class.java.simpleName)) {
                    delegatedProperty = "requiredExtraOf(extras.${it.simpleName.getShortName()})"
                }

                bindingProperties.append(
                    """
                    |$declaringProperty ${it.simpleName.getShortName()} by $delegatedProperty
                    |   
                    """.trimMargin()
                )
                routerProperties.append(
                    """
                    |var ${it.simpleName.getShortName()} by $delegatedProperty
                    |   
                    """.trimMargin()
                )
            }

            val outputStream: OutputStream = codeGenerator.createNewFile(
                dependencies = dependencies,
                packageName = packageName,
                fileName = fileNameGenerated
            )

            outputStream.write(
                """
                |package $packageName
                |
                |import android.content.Context
                |import android.content.Intent
                |import android.os.Bundle
                |import com.ktan.extraOf
                |import com.ktan.requiredExtraOf
                |import com.ktan.binding.ExtrasBinding
                |import com.ktan.router.KtanRouter
                |
                |class ${className}Binding(extras: $className = $className()) : ExtrasBinding() {
                |
                |   $bindingProperties
                |}
                |
                |class ${className}Router(block: ${className}Router.() -> Unit) : KtanRouter() {
                |
                |   private val extras: $className = $className()
                |
                |   $routerProperties
                |   init {
                |       block.invoke(this)
                |   }
                |
                |}
                |
                |fun Intent.populate${className}(block: ${className}Router.() -> Unit): Intent {
                |   ${className}Router(block).populate(this)
                |   return this;
                |}
                |
                |fun Bundle.populate${className}(block: ${className}Router.() -> Unit): Bundle {
                |   ${className}Router(block).populate(this)
                |   return this;
                |}
                """.trimMargin().toByteArray()
            )

            logger.info("File $fileNameGenerated generated")
        }
    }

    private inner class RouteKClassVisitor(val dependencies: Dependencies) : KSVisitorVoid() {

        override fun visitClassDeclaration(
            classDeclaration:
            KSClassDeclaration, data: Unit
        ) {
            if (classDeclaration.isAbstract()) {
                logger.error(
                    "||Class Annotated with Extras should not abstract", classDeclaration
                )
            }

            val className = classDeclaration.simpleName.getShortName()
            val packageName = classDeclaration.packageName.asString()
            val classPackage = "$packageName.$className"
            val routeAnnotation =
                classDeclaration.annotations.getAnnotations(Route::class.java.simpleName)
            val fileNameGenerated = "${className}Route"

            if (routeAnnotation == null) {
                logger.error(
                    "||Extras not found in $className"
                )
            }

            val routeExtrasAnnotation =
                routeAnnotation?.arguments?.filter { it.validate() }
                    ?.first { it.name?.getShortName() == "extras" }
            val routeExtrasValue = routeExtrasAnnotation?.value as List<KSType>

            logger.info(
                "package $classPackage with args $routeExtrasAnnotation=$routeExtrasValue",
                classDeclaration
            )

            val importDependencies = StringBuilder()
            val functionGenerated = StringBuilder()
            val blockArgs = StringBuilder()
            val blockArgsInput = StringBuilder()
            val populateBlocks = StringBuilder()
            val funcPrefix = "routeTo"

            routeExtrasValue.forEach { it ->
                val extrasValueClassName = it.declaration.simpleName.getShortName()
                val extrasValuePackageName = it.declaration.packageName.asString()
                val routerClassName = "${extrasValueClassName}Router"
                val routerClassNameBlock =
                    "${routerClassName.replaceFirstChar { it.lowercase() }}Block"
                val funcPopulateName = "populate${extrasValueClassName}"

                importDependencies.append(
                    """
                    |import $extrasValuePackageName.$extrasValueClassName
                    |import $extrasValuePackageName.$routerClassName
                    |import $extrasValuePackageName.$funcPopulateName
                    |
                    """.trimMargin()
                )
                blockArgs.append(
                    """
                    |
                    |   ${routerClassNameBlock}: ${routerClassName}.() -> Unit,
                    """.trimMargin()
                )
                blockArgsInput.append(
                    """
                    |
                    |   ${routerClassNameBlock},
                    """.trimMargin()
                )
                populateBlocks.append(
                    """
                    |
                    |       .$funcPopulateName($routerClassNameBlock)
                    """.trimMargin()
                )
            }

            importDependencies.append(
                """
                |import $classPackage
                |
                """.trimMargin()
            )

            if (classDeclaration.getAllSuperTypes().firstOrNull {
                    it.declaration.simpleName.getShortName() == "AppCompatActivity"
                } != null) {
                importDependencies.append(
                    """
                    |import android.app.Activity
                    |import android.content.Context
                    |import android.content.Intent
                    |import androidx.fragment.app.Fragment
                    |
                    """.trimMargin()
                )
                functionGenerated.append(
                    """
                    |fun $funcPrefix$className(
                    |   context: Context,$blockArgs
                    |): Intent {
                    |   return Intent(context, $className::class.java)$populateBlocks
                    |}
                    |
                    |fun Activity.$funcPrefix$className($blockArgs
                    |): Intent = $funcPrefix$className(
                    |   this,$blockArgsInput
                    |)
                    |
                    |fun Fragment.$funcPrefix$className($blockArgs
                    |): Intent? = context?.let { $funcPrefix$className( 
                    |   it,$blockArgsInput
                    |)} 
                    |
                    """.trimMargin()
                )
            } else if (classDeclaration.getAllSuperTypes().firstOrNull {
                    it.declaration.simpleName.getShortName() == "Fragment"
                } != null) {
                importDependencies.append(
                    """
                    |import android.content.Context
                    |import android.os.Bundle
                    |import androidx.fragment.app.Fragment
                    |
                    """.trimMargin()
                )
                functionGenerated.append(
                    """
                    |fun $funcPrefix$className($blockArgs
                    |): $className {
                    |   return ${className}().apply {
                    |       arguments = Bundle()$populateBlocks
                    |   }
                    |}
                    |
                    """.trimMargin()
                )
            }

            val outputStream: OutputStream = codeGenerator.createNewFile(
                dependencies = dependencies,
                packageName = packageName,
                fileName = fileNameGenerated
            )

            outputStream.write(
                """
                |package $packageName
                |
                |$importDependencies
                |$functionGenerated
                """.trimMargin().toByteArray()
            )

            logger.info("File $fileNameGenerated generated")
        }
    }
}

fun Sequence<KSAnnotation>.getAnnotations(name: String): KSAnnotation? = firstOrNull {
    it.shortName.getShortName() == name
}

fun Sequence<KSAnnotation>.isAnnotationPresent(name: String): Boolean = getAnnotations(name) != null

fun KSValueParameter.isNotKotlinPrimitive(): Boolean {

    return when (type.element?.toString()) {
        "String", "Int", "Short", "Number", "Boolean", "Byte", "Char", "Float", "Double", "Long", "Unit", "Any" -> false
        else -> true
    }
}

fun KSValueParameter.getPrimitiveTypeName(): String {

    return type.element?.toString() ?: throw IllegalAccessException()
}

class KtanKSValidateVisitor : KSValidateVisitor({ _, _ -> true }) {

    override fun visitTypeReference(
        typeReference: KSTypeReference,
        data: KSNode?
    ): Boolean {
        return true
    }
}