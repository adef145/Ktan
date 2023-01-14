package com.ktan.processor.properties

import com.google.devtools.ksp.symbol.KSAnnotation

interface KtanPropertyAdapter {

    fun isMatch(annotations: Sequence<KSAnnotation>): Boolean

    fun onInit(name: String)

    fun onMutableIsPresent()

    fun onRequiredIsPresent()

    fun build()
}