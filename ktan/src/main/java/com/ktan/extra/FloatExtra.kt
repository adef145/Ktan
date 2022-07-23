package com.ktan.extra

import android.content.Intent
import android.os.Bundle

class FloatExtra @JvmOverloads constructor(
    key: String,
    value: Float? = null
) : AbstractExtra<Float?>(
    key,
    value
) {
    override fun put(bundle: Bundle) {
        value?.also { bundle.putFloat(key, it) }
    }

    override fun put(intent: Intent) {
        intent.putExtra(key, value)
    }

    override fun get(bundle: Bundle): Float {
        return value?.let { bundle.getFloat(key, it) } ?: bundle.getFloat(key)
    }
}