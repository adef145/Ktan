package com.ktan.flow

import com.ktan.Ktan
import com.ktan.extra.MutableExtra
import com.ktan.provideGetterExtra
import com.ktan.provideGetterRequiredExtra
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun <T, E : MutableExtra<T?>> Ktan.stateFlowExtraOf(
    extra: E,
    defaultValue: T? = null
): ReadOnlyProperty<Ktan, StateFlow<T?>> {
    onInitiateExtraProperty(extra)
    return StateFlowExtraReadOnlyProperty {
        MutableFlowExtra(
            extra,
            MutableStateFlow(provideGetterExtra(defaultValue).get(this, extra))
        )
    }
}

fun <T, E : MutableExtra<T?>> Ktan.sharedFlowExtraOf(
    extra: E,
    defaultValue: T? = null
): ReadOnlyProperty<Ktan, SharedFlow<T?>> {
    onInitiateExtraProperty(extra)
    return StateFlowExtraReadOnlyProperty {
        MutableFlowExtra(
            extra,
            MutableStateFlow(provideGetterExtra(defaultValue).get(this, extra))
        )
    }
}

fun <T, E : MutableExtra<T?>> Ktan.mutableStateFlowExtraOf(
    extra: E,
    defaultValue: T? = null
): ReadOnlyProperty<Ktan, MutableStateFlow<T?>> {
    onInitiateExtraProperty(extra)
    return StateFlowExtraReadOnlyProperty {
        MutableFlowExtra(
            extra,
            MutableStateFlow(provideGetterExtra(defaultValue).get(this, extra))
        )
    }
}

fun <T, E : MutableExtra<T?>> Ktan.mutableSharedFlowExtraOf(
    extra: E,
    defaultValue: T? = null
): ReadOnlyProperty<Ktan, MutableSharedFlow<T?>> {
    onInitiateExtraProperty(extra)
    return StateFlowExtraReadOnlyProperty {
        MutableFlowExtra(
            extra,
            MutableStateFlow(provideGetterExtra(defaultValue).get(this, extra))
        )
    }
}

// REQUIRED

fun <T, E : MutableExtra<T?>> Ktan.requiredStateFlowExtraOf(
    extra: E,
    defaultValue: T? = null
): ReadOnlyProperty<Ktan, StateFlow<T>> {
    onInitiateExtraProperty(extra)
    return StateFlowExtraReadOnlyProperty {
        MutableFlowExtra(
            extra,
            MutableStateFlow(provideGetterRequiredExtra(defaultValue).get(this, extra))
        )
    }
}

fun <T, E : MutableExtra<T?>> Ktan.requiredSharedFlowExtraOf(
    extra: E,
    defaultValue: T? = null
): ReadOnlyProperty<Ktan, SharedFlow<T>> {
    onInitiateExtraProperty(extra)
    return StateFlowExtraReadOnlyProperty {
        MutableFlowExtra(
            extra,
            MutableStateFlow(provideGetterRequiredExtra(defaultValue).get(this, extra))
        )
    }
}

fun <T, E : MutableExtra<T?>> Ktan.requiredMutableStateFlowExtraOf(
    extra: E,
    defaultValue: T? = null
): ReadOnlyProperty<Ktan, MutableStateFlow<T>> {
    onInitiateExtraProperty(extra)
    return StateFlowExtraReadOnlyProperty {
        MutableFlowExtra(
            extra,
            MutableStateFlow(provideGetterRequiredExtra(defaultValue).get(this, extra))
        )
    }
}

fun <T, E : MutableExtra<T?>> Ktan.requiredMutableSharedFlowExtraOf(
    extra: E,
    defaultValue: T? = null
): ReadOnlyProperty<Ktan, MutableSharedFlow<T>> {
    onInitiateExtraProperty(extra)
    return StateFlowExtraReadOnlyProperty {
        MutableFlowExtra(
            extra,
            MutableStateFlow(provideGetterRequiredExtra(defaultValue).get(this, extra))
        )
    }
}

private class StateFlowExtraReadOnlyProperty<T>(
    block: () -> MutableFlowExtra<T>
) : ReadOnlyProperty<Ktan, MutableFlowExtra<T>> {

    val value: MutableFlowExtra<T> by lazy {
        block.invoke()
    }

    override fun getValue(thisRef: Ktan, property: KProperty<*>): MutableFlowExtra<T> {
        return value
    }
}