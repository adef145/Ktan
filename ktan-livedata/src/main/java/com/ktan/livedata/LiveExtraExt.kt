package com.ktan.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ktan.Ktan
import com.ktan.extra.Extra
import com.ktan.extra.MutableExtra
import com.ktan.provideGetterExtra
import com.ktan.provideGetterRequiredExtra
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun <T, E : MutableExtra<T?>> Ktan.liveExtraOf(
    extra: E,
    defaultValue: T? = null
): ReadOnlyProperty<Ktan, LiveData<T?>> {
    onInitiateExtraProperty(extra)
    return LiveExtraReadOnlyProperty {
        MutableLiveExtra(extra, provideGetterExtra(defaultValue).get(this, extra))
    }
}

fun <T, E : MutableExtra<T?>> Ktan.mutableLiveExtraOf(
    extra: E,
    defaultValue: T? = null
): ReadOnlyProperty<Ktan, MutableLiveData<T?>> {
    onInitiateExtraProperty(extra)
    return LiveExtraReadOnlyProperty {
        MutableLiveExtra(extra, provideGetterExtra(defaultValue).get(this, extra))
    }
}

fun <T, E : MutableExtra<T?>> Ktan.requiredLiveExtraOf(
    extra: E,
    defaultValue: T? = null
): ReadOnlyProperty<Ktan, LiveData<T>> {
    onInitiateExtraProperty(extra)
    return LiveExtraReadOnlyProperty {
        MutableLiveExtra(extra, provideGetterRequiredExtra(defaultValue).get(this, extra))
    }
}

fun <T, E : MutableExtra<T?>> Ktan.requiredMutableLiveExtraOf(
    extra: E,
    defaultValue: T? = null
): ReadOnlyProperty<Ktan, MutableLiveData<T>> {
    onInitiateExtraProperty(extra)
    return LiveExtraReadOnlyProperty {
        MutableLiveExtra(extra, provideGetterRequiredExtra(defaultValue).get(this, extra))
    }
}

private class LiveExtraReadOnlyProperty<T>(
    block: () -> MutableLiveExtra<T>
) : ReadOnlyProperty<Ktan, MutableLiveExtra<T>> {

    val value: MutableLiveExtra<T> by lazy {
        block.invoke()
    }

    override fun getValue(thisRef: Ktan, property: KProperty<*>): MutableLiveExtra<T> {
        return value
    }
}