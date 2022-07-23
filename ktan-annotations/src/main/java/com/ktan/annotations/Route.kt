package com.ktan.annotations

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Route(
    val extras: Array<KClass<*>>
)
