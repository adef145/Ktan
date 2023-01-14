package com.ktan.processor

abstract class KtanClassDeclaration : KtanBuilder<ByteArray> {

    protected val importDependencies = StringBuilder()

    fun appendDependencies(prefix: String? = null, vararg values: String) {
        values.forEach {
            importDependencies.append(
                """
                |
                |import ${prefix?.plus(".") ?: ""}$it
                """.trimMargin()
            )
        }
    }
}