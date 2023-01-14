package com.ktan.processor

class KtanRouteClassDeclaration(
    private val className: String,
    private val packageName: String
) : KtanClassDeclaration() {

    companion object {
        private const val FUNC_PREFIX = "routeTo"
    }

    private val classPackage = "$packageName.$className"
    private val functionGenerated = StringBuilder()
    private val blockArgs = StringBuilder()
    private val blockArgsInput = StringBuilder()
    private val populateBlocks = StringBuilder()

    init {
        appendDependencies(
            values = arrayOf(classPackage)
        )
    }

    fun bindRouteExtrasValue(
        extrasValueClassName: String,
        extrasValuePackageName: String
    ) {
        val routerClassName = "${extrasValueClassName}Router"
        val routerClassNameBlock =
            "${routerClassName.replaceFirstChar { it.lowercase() }}Block"
        val funcPopulateName = "populate${extrasValueClassName}"

        appendDependencies(
            prefix = extrasValuePackageName,
            values = arrayOf(
                extrasValueClassName,
                routerClassName,
                funcPopulateName
            )
        )
        blockArgs.append(
            """
            |
            |   ${routerClassNameBlock}: ${routerClassName}.() -> Unit,
            """.trimMargin()
        )
        blockArgsInput.append(
            """
            |
            |   ${routerClassNameBlock},
            """.trimMargin()
        )
        populateBlocks.append(
            """
            |
            |       .$funcPopulateName($routerClassNameBlock)
            """.trimMargin()
        )
    }

    fun onActivityIsParent() {
        appendDependencies(
            values = arrayOf(
                "android.app.Activity",
                "android.content.Context",
                "android.content.Intent",
                "androidx.fragment.app.Fragment"
            )
        )
        functionGenerated.append(
            """
            |fun $FUNC_PREFIX$className(
            |   context: Context,$blockArgs
            |): Intent {
            |   return Intent(context, $className::class.java)$populateBlocks
            |}
            |
            |fun Activity.$FUNC_PREFIX$className($blockArgs
            |): Intent = $FUNC_PREFIX$className(
            |   this,$blockArgsInput
            |)
            |
            |fun Fragment.$FUNC_PREFIX$className($blockArgs
            |): Intent? = context?.let { $FUNC_PREFIX$className( 
            |   it,$blockArgsInput
            |)} 
            |
            """.trimMargin()
        )
    }

    fun onFragmentIsParent() {
        appendDependencies(
            values = arrayOf(
                "android.content.Context",
                "android.os.Bundle",
                "androidx.fragment.app.Fragment"
            )
        )
        functionGenerated.append(
            """
            |fun $FUNC_PREFIX$className($blockArgs
            |): $className {
            |   return ${className}().apply {
            |       arguments = Bundle()$populateBlocks
            |   }
            |}
            |
            """.trimMargin()
        )
    }

    override fun build(): ByteArray =
        """
        |package $packageName
        |$importDependencies
        |
        |$functionGenerated
        """.trimMargin().toByteArray()

    override fun toString(): String {
        return classPackage
    }
}