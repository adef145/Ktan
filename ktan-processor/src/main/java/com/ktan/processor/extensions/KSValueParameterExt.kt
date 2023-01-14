package com.ktan.processor.extensions

import com.google.devtools.ksp.symbol.KSValueParameter

fun KSValueParameter.isNotKotlinPrimitive(): Boolean {

    return when (type.element?.toString()) {
        "String", "Int", "Short", "Number", "Boolean", "Byte", "Char", "Float", "Double", "Long", "Unit", "Any" -> false
        else -> true
    }
}

fun KSValueParameter.getPrimitiveTypeName(): String {

    return type.element?.toString() ?: throw IllegalAccessException()
}