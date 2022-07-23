package com.ktan.extra

import android.content.Intent
import android.os.Bundle

class DoubleExtra @JvmOverloads constructor(
    key: String,
    value: Double? = null
) : AbstractExtra<Double?>(
    key,
    value
) {
    override fun put(bundle: Bundle) {
        value?.also { bundle.putDouble(key, it) }
    }

    override fun put(intent: Intent) {
        intent.putExtra(key, value)
    }

    override fun get(bundle: Bundle): Double {
        return value?.let { bundle.getDouble(key, it) } ?: bundle.getDouble(key)
    }
}