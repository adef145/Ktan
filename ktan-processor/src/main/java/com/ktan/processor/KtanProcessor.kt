package com.ktan.processor

import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.isAbstract
import com.google.devtools.ksp.isOpen
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.validate
import com.ktan.annotations.Extras
import com.ktan.annotations.Route
import com.ktan.processor.extensions.getAnnotations
import com.ktan.processor.extensions.isActivityPresent
import com.ktan.processor.extensions.isFragmentPresent
import com.ktan.processor.extensions.isMutablePresent
import com.ktan.processor.extensions.isRequiredPresent
import com.ktan.processor.integrations.flow.FlowPropertyAdapter
import com.ktan.processor.integrations.livedata.LiveDataPropertyAdapter
import com.ktan.processor.properties.DefaultPropertyAdapter
import java.io.OutputStream

class KtanProcessor(environment: SymbolProcessorEnvironment) : SymbolProcessor {

    companion object {

        private const val OPTIONS_PREFIX = "com.ktan.processor"

        private const val OPTIONS_INTEGRATIONS = "$OPTIONS_PREFIX.integrations"
    }

    private val logger = environment.logger

    private val codeGenerator = environment.codeGenerator

    private val options = environment.options

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
        val extrasKClassVisitor = ExtrasKClassVisitor(
            dependencies,
            options[OPTIONS_INTEGRATIONS]?.let { OPTIONS_INTEGRATIONS.plus(".$it") }
        )
        val routeKClassVisitor = RouteKClassVisitor(dependencies)

        extrasSymbolsToProcess.forEach {
            it.accept(extrasKClassVisitor, Unit)
        }
        routeSymbolsToProcess
            .forEach { it.accept(routeKClassVisitor, Unit) }

        return unableToProcess.toList()
    }

    private inner class ExtrasKClassVisitor(
        val dependencies: Dependencies,
        val integrationOption: String?
    ) : KSVisitorVoid() {

        override fun visitClassDeclaration(
            classDeclaration:
            KSClassDeclaration, data: Unit
        ) {
            // Validate abstract class
            if (classDeclaration.isAbstract()) {
                logger.error(
                    "||Class Annotated with Extras should not abstract", classDeclaration
                )
            }

            // Validate open class
            if (classDeclaration.isOpen()) {
                logger.error(
                    "||Class Annotated with Projections should kotlin data class", classDeclaration
                )
            }

            // region Prerequisite
            val className = classDeclaration.simpleName.getShortName()
            val packageName = classDeclaration.packageName.asString()
            val fileNameGenerated = "${className}Ktan"
            val ktanExtrasClassDeclaration = KtanExtrasClassDeclaration(className, packageName)
            val adapters = listOf(
                LiveDataPropertyAdapter(
                    integrationOption,
                    ktanExtrasClassDeclaration
                ),
                FlowPropertyAdapter(
                    integrationOption,
                    ktanExtrasClassDeclaration
                ),
                DefaultPropertyAdapter(ktanExtrasClassDeclaration)
            )
            logger.info("package $ktanExtrasClassDeclaration", classDeclaration)
            // endregion Prerequisite

            // Looping all declared properties / fields
            classDeclaration.getDeclaredProperties().filter { it.validate() }.forEach {
                // Get name of property / field
                val name = it.simpleName.getShortName()

                logger.info("$name found with type ${it.type}")

                // Get adapter base on matching rule
                val adapter = adapters.first { adapter -> adapter.isMatch(it.annotations) }

                // Do init first
                adapter.onInit(it.simpleName.getShortName())

                // Setup adapter if mutable annotation present
                if (it.annotations.isMutablePresent()) {
                    adapter.onMutableIsPresent()
                }

                // Setup adapter if required annotation present
                if (it.annotations.isRequiredPresent()) {
                    adapter.onRequiredIsPresent()
                }

                // Build adapter
                adapter.build()
            }

            // Create file
            val outputStream: OutputStream = codeGenerator.createNewFile(
                dependencies = dependencies,
                packageName = packageName,
                fileName = fileNameGenerated
            )

            // Write file
            outputStream.write(ktanExtrasClassDeclaration.build())

            logger.info("File $fileNameGenerated generated")
        }
    }

    private inner class RouteKClassVisitor(val dependencies: Dependencies) : KSVisitorVoid() {

        override fun visitClassDeclaration(
            classDeclaration:
            KSClassDeclaration, data: Unit
        ) {
            // Validate abstract class
            if (classDeclaration.isAbstract()) {
                logger.error(
                    "||Class Annotated with Extras should not abstract", classDeclaration
                )
            }

            // region Prerequisites
            val className = classDeclaration.simpleName.getShortName()
            val packageName = classDeclaration.packageName.asString()
            val routeAnnotation =
                classDeclaration.annotations.getAnnotations(Route::class) ?: return
            val fileNameGenerated = "${className}Route"
            val ktanRouteClassDeclaration = KtanRouteClassDeclaration(
                className,
                packageName
            )
            val routeExtrasAnnotation = routeAnnotation.arguments.filter { it.validate() }
                .first { it.name?.getShortName() == "extras" }
            val routeExtrasValue = routeExtrasAnnotation.value as List<KSType>
            // endregion Prerequisites

            logger.info(
                "package $ktanRouteClassDeclaration with args $routeExtrasAnnotation=$routeExtrasValue",
                classDeclaration
            )

            // Bind each route extras value
            routeExtrasValue.forEach {
                ktanRouteClassDeclaration.bindRouteExtrasValue(
                    it.declaration.simpleName.getShortName(),
                    it.declaration.packageName.asString()
                )
            }

            // Setup when parent is Activity / Fragment
            if (classDeclaration.getAllSuperTypes().isActivityPresent()) {
                ktanRouteClassDeclaration.onActivityIsParent()
            } else if (classDeclaration.getAllSuperTypes().isFragmentPresent()) {
                ktanRouteClassDeclaration.onFragmentIsParent()
            }

            // Create file
            val outputStream: OutputStream = codeGenerator.createNewFile(
                dependencies = dependencies,
                packageName = packageName,
                fileName = fileNameGenerated
            )

            // Write file
            outputStream.write(ktanRouteClassDeclaration.build())

            logger.info("File $fileNameGenerated generated")
        }
    }
}