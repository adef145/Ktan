package com.ktan.extra

import android.content.Intent
import android.os.Bundle

class FloatArrayExtra @JvmOverloads constructor(
    key: String,
    value: FloatArray? = null
) : AbstractExtra<FloatArray?>(
    key,
    value
) {
    override fun put(bundle: Bundle) {
        value?.also { bundle.putFloatArray(key, it) }
    }

    override fun put(intent: Intent) {
        intent.putExtra(key, value)
    }

    override fun get(bundle: Bundle): FloatArray? {
        return bundle.getFloatArray(key) ?: value
    }
}