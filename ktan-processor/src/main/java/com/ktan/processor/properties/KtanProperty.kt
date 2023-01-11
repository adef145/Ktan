package com.ktan.processor.properties

import com.ktan.processor.KtanBuilder

class KtanProperty(
    private var delegate: String,
    private val name: String
) : KtanBuilder<String> {

    private var value = "extraOf(extras.${name})"

    fun setToVar() {
        delegate = "var"
    }

    fun append(prefix: String) {
        value = append(prefix, value)
    }

    override fun build(): String = """
        |$delegate $name by $value
        |   
    """.trimMargin()

    private fun append(prefix: String, source: String) = "$prefix${
        source.replaceFirstChar { first ->
            first.uppercase()
        }
    }"
}