package com.ktan.processor.integrations.flow

import com.google.devtools.ksp.symbol.KSAnnotation
import com.ktan.flow.annotations.FlowExtra
import com.ktan.processor.KtanExtrasClassDeclaration
import com.ktan.processor.extensions.isAnnotationPresent
import com.ktan.processor.properties.DefaultPropertyAdapter

class FlowPropertyAdapter(
    classDeclaration: KtanExtrasClassDeclaration
) : DefaultPropertyAdapter(classDeclaration) {

    override fun isMatch(annotations: Sequence<KSAnnotation>): Boolean {
        return annotations.isAnnotationPresent(FlowExtra::class)
    }

    override fun onInit(name: String) {
        if (isFirstInit) {
            classDeclaration.appendDependencies(
                prefix = "com.ktan.flow",
                values = arrayOf(
                    "flowExtraOf",
                    "mutableFlowExtraOf",
                    "requiredFlowExtraOf",
                    "requiredMutableFlowExtraOf"
                )
            )
        }
        super.onInit(name)

        bindingProperty.append("flow")
    }

    override fun onMutableIsPresent() {
        bindingProperty.append("mutable")
    }
}