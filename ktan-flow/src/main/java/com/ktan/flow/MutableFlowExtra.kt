package com.ktan.flow

import com.ktan.extra.MutableExtra
import kotlinx.coroutines.flow.MutableStateFlow

open class MutableFlowExtra<T>(
    private val extra: MutableExtra<T?>,
    private val flow: MutableStateFlow<T>
) : MutableStateFlow<T> by flow {

    override suspend fun emit(value: T) {
        extra.set(value)
        flow.emit(value)
    }
}