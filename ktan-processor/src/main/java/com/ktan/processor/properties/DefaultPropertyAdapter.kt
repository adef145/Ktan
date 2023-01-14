package com.ktan.processor.properties

import com.google.devtools.ksp.symbol.KSAnnotation
import com.ktan.processor.KtanExtrasClassDeclaration

open class DefaultPropertyAdapter(
    protected val classDeclaration: KtanExtrasClassDeclaration
) : KtanPropertyAdapter {

    protected lateinit var bindingProperty: KtanProperty

    private lateinit var routerProperty: KtanProperty

    protected open val isFirstInit
        get() = !this::bindingProperty.isInitialized && !this::routerProperty.isInitialized

    override fun isMatch(annotations: Sequence<KSAnnotation>): Boolean {
        return true
    }

    override fun onInit(name: String) {
        bindingProperty = KtanProperty("val", name)
        routerProperty = KtanProperty("var", name)
    }

    override fun onMutableIsPresent() {
        bindingProperty.setToVar()
    }

    override fun onRequiredIsPresent() {
        bindingProperty.append("required")
        routerProperty.append("required")
    }

    override fun build() {
        classDeclaration.appendProperty(bindingProperty.build(), routerProperty.build())
    }
}