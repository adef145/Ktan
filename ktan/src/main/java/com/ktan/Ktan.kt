package com.ktan

import android.app.Application
import android.os.Bundle
import com.ktan.binding.ExtrasBinding
import com.ktan.extra.Extra
import com.ktan.extra.MutableExtra
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

// region Ktan

interface Ktan {

    fun onInitiateExtraProperty(extra: Extra<*>)

    companion object {

        private val ktanLifecycleCallbacks: KtanLifecycleCallbacks by lazy { KtanLifecycleCallbacks() }

        @JvmStatic
        fun init(application: Application) {
            application.registerActivityLifecycleCallbacks(ktanLifecycleCallbacks)
        }

        internal fun getSavedInstance(obj: Any): Bundle? {
            return ktanLifecycleCallbacks.savedInstanceMap[obj]
        }

        internal fun registerBinding(obj: Any, ktanBinding: ExtrasBinding) {
            ktanLifecycleCallbacks.registerKtanBinding(obj, ktanBinding)
        }
    }
}

// endregion

// region Property

@JvmOverloads
fun <T, E : MutableExtra<T?>> Ktan.extraOf(
    extra: E,
    defaultValue: T? = null
): ReadWriteProperty<Ktan, T?> {
    onInitiateExtraProperty(extra)
    return ExtraReadWriteProperty(extra, provideGetterExtra(defaultValue), provideSetterExtra())
}

@JvmOverloads
fun <T, E : MutableExtra<T?>> Ktan.requiredExtraOf(
    extra: E,
    defaultValue: T? = null
): ReadWriteProperty<Ktan, T> {
    onInitiateExtraProperty(extra)
    return ExtraReadWriteProperty(
        extra,
        provideGetterRequiredExtra(defaultValue),
        provideSetterExtra()
    )
}

@JvmOverloads
fun <T, E : Extra<T?>> provideGetterExtra(defaultValue: T? = null): ReadOnlyExtra<T?, E> =
    object : ReadOnlyExtra<T?, E> {
        override fun get(ktan: Ktan, extra: E): T? {
            return extra.get() ?: defaultValue
        }

    }

@JvmOverloads
fun <T, E : Extra<T?>> provideGetterRequiredExtra(defaultValue: T? = null): ReadOnlyExtra<T, E> =
    object : ReadOnlyExtra<T, E> {
        override fun get(ktan: Ktan, extra: E): T {
            return extra.get() ?: defaultValue
            ?: throw IllegalStateException("Extra value and default value is null")
        }
    }

fun <T, E : MutableExtra<T?>> provideSetterExtra(): WriteOnlyExtra<T, E> =
    object : WriteOnlyExtra<T, E> {
        override fun set(ktan: Ktan, extra: E, value: T) {
            extra.set(value)
        }
    }

interface ReadOnlyExtra<T, E> {

    fun get(ktan: Ktan, extra: E): T
}

interface WriteOnlyExtra<T, E> {

    fun set(ktan: Ktan, extra: E, value: T)
}

private class ExtraReadWriteProperty<T, E : Extra<T?>>(
    val extra: E,
    val getter: ReadOnlyExtra<T, E>,
    val setter: WriteOnlyExtra<T, E>
) : ReadWriteProperty<Ktan, T> {

    override fun setValue(thisRef: Ktan, property: KProperty<*>, value: T) {
        setter.set(thisRef, extra, value)
    }

    override fun getValue(thisRef: Ktan, property: KProperty<*>): T {
        return getter.get(thisRef, extra)
    }
}

// endregion
