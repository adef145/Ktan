package com.ktan.extra

import android.os.Bundle

abstract class AbstractExtra<T> @JvmOverloads constructor(val key: String, protected var value: T? = null) : Extra<T>,
    MutableExtra<T> {

    override fun get(): T? {
        return value
    }

    override fun set(value: T) {
        this.value = value
    }

    override fun bind(vararg bundles: Bundle?) {
        value = bundles.firstOrNull { it?.containsKey(key) == true }?.let { get(it) } ?: value
    }

    protected abstract fun get(bundle: Bundle): T
}