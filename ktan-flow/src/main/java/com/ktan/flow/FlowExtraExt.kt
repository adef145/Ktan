package com.ktan.flow

import com.ktan.Ktan
import com.ktan.extra.MutableExtra
import com.ktan.provideGetterExtra
import com.ktan.provideGetterRequiredExtra
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun <T, E : MutableExtra<T?>> Ktan.flowExtraOf(
    extra: E,
    defaultValue: T? = null
): ReadOnlyProperty<Ktan, StateFlow<T?>> {
    onInitiateExtraProperty(extra)
    return FlowExtraReadOnlyProperty {
        MutableFlowExtra(
            extra,
            MutableStateFlow(provideGetterExtra(defaultValue).get(this, extra))
        )
    }
}

fun <T, E : MutableExtra<T?>> Ktan.mutableFlowExtraOf(
    extra: E,
    defaultValue: T? = null
): ReadOnlyProperty<Ktan, MutableStateFlow<T?>> {
    onInitiateExtraProperty(extra)
    return FlowExtraReadOnlyProperty {
        MutableFlowExtra(
            extra,
            MutableStateFlow(provideGetterExtra(defaultValue).get(this, extra))
        )
    }
}

// REQUIRED

fun <T, E : MutableExtra<T?>> Ktan.requiredFlowExtraOf(
    extra: E,
    defaultValue: T? = null
): ReadOnlyProperty<Ktan, StateFlow<T>> {
    onInitiateExtraProperty(extra)
    return FlowExtraReadOnlyProperty {
        MutableFlowExtra(
            extra,
            MutableStateFlow(provideGetterRequiredExtra(defaultValue).get(this, extra))
        )
    }
}

fun <T, E : MutableExtra<T?>> Ktan.requiredMutableFlowExtraOf(
    extra: E,
    defaultValue: T? = null
): ReadOnlyProperty<Ktan, MutableStateFlow<T>> {
    onInitiateExtraProperty(extra)
    return FlowExtraReadOnlyProperty {
        MutableFlowExtra(
            extra,
            MutableStateFlow(provideGetterRequiredExtra(defaultValue).get(this, extra))
        )
    }
}

private class FlowExtraReadOnlyProperty<T>(
    block: () -> MutableFlowExtra<T>
) : ReadOnlyProperty<Ktan, MutableFlowExtra<T>> {

    val value: MutableFlowExtra<T> by lazy {
        block.invoke()
    }

    override fun getValue(thisRef: Ktan, property: KProperty<*>): MutableFlowExtra<T> {
        return value
    }
}