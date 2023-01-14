package com.ktan.processor

class KtanExtrasClassDeclaration(
    private val className: String,
    private val packageName: String
) : KtanClassDeclaration() {

    private val classPackage = "$packageName.$className"
    private val bindingProperties = StringBuilder()
    private val routerProperties = StringBuilder()

    init {
        appendDependencies(
            values = arrayOf(
                "android.content.Context",
                "android.content.Intent",
                "android.os.Bundle",
                "com.ktan.extraOf",
                "com.ktan.requiredExtraOf",
                "com.ktan.binding.ExtrasBinding",
                "com.ktan.router.KtanRouter"
            )
        )
    }

    fun appendProperty(binding: String, router: String) {
        bindingProperties.append(binding)
        routerProperties.append(router)
    }

    override fun build(): ByteArray =
        """
        |package $packageName
        |
        |$importDependencies
        |
        |class ${className}Binding(extras: $className = $className()) : ExtrasBinding() {
        |
        |   $bindingProperties
        |}
        |
        |class ${className}Router(block: ${className}Router.() -> Unit) : KtanRouter() {
        |
        |   private val extras: $className = $className()
        |
        |   $routerProperties
        |   init {
        |       block.invoke(this)
        |   }
        |
        |}
        |
        |fun Intent.populate${className}(block: ${className}Router.() -> Unit): Intent {
        |   ${className}Router(block).populate(this)
        |   return this;
        |}
        |
        |fun Bundle.populate${className}(block: ${className}Router.() -> Unit): Bundle {
        |   ${className}Router(block).populate(this)
        |   return this;
        |}
        """.trimMargin().toByteArray()

    override fun toString(): String {
        return classPackage
    }
}