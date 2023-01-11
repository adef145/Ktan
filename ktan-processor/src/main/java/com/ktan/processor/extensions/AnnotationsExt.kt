package com.ktan.processor.extensions

import com.google.devtools.ksp.symbol.KSAnnotation
import com.ktan.annotations.Mutable
import com.ktan.annotations.Required
import kotlin.reflect.KClass

// region Public

fun Sequence<KSAnnotation>.isRequiredPresent() = isAnnotationPresent(Required::class)

fun Sequence<KSAnnotation>.isMutablePresent() = isAnnotationPresent(Mutable::class)

fun <T : Any> Sequence<KSAnnotation>.isAnnotationPresent(clazz: KClass<T>): Boolean =
    isAnnotationPresent(clazz.java.simpleName)

fun <T : Any> Sequence<KSAnnotation>.getAnnotations(clazz: KClass<T>): KSAnnotation? =
    getAnnotations(clazz.java.simpleName)

// endregion Public

// region Private

private fun Sequence<KSAnnotation>.isAnnotationPresent(name: String): Boolean =
    getAnnotations(name) != null

private fun Sequence<KSAnnotation>.getAnnotations(name: String): KSAnnotation? = firstOrNull {
    it.shortName.getShortName() == name
}

// endregion Private