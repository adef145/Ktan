package com.ktan.processor.integrations.livedata

import com.google.devtools.ksp.symbol.KSAnnotation
import com.happyfresh.ktan.livedata.annotations.LiveExtra
import com.ktan.processor.KtanExtrasClassDeclaration
import com.ktan.processor.extensions.isAnnotationPresent
import com.ktan.processor.properties.DefaultPropertyAdapter

class LiveDataPropertyAdapter(
    private val integrationOption: String?,
    classDeclaration: KtanExtrasClassDeclaration
) : DefaultPropertyAdapter(classDeclaration) {

    override fun isMatch(annotations: Sequence<KSAnnotation>): Boolean {
        return integrationOption == this.javaClass.packageName ||
            annotations.isAnnotationPresent(LiveExtra::class)
    }

    override fun onInit(name: String) {
        if (isFirstInit) {
            classDeclaration.appendDependencies(
                prefix = "com.ktan.livedata",
                values = arrayOf(
                    "liveExtraOf",
                    "mutableLiveExtraOf",
                    "requiredLiveExtraOf",
                    "requiredMutableLiveExtraOf"
                )
            )
        }

        super.onInit(name)

        bindingProperty.append("live")
    }

    override fun onMutableIsPresent() {
        bindingProperty.append("mutable")
    }
}