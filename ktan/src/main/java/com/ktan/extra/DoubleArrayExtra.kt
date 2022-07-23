package com.ktan.extra

import android.content.Intent
import android.os.Bundle

class DoubleArrayExtra @JvmOverloads constructor(
    key: String,
    value: DoubleArray? = null
) : AbstractExtra<DoubleArray?>(
    key,
    value
) {
    override fun put(bundle: Bundle) {
        value?.also { bundle.putDoubleArray(key, it) }
    }

    override fun put(intent: Intent) {
        intent.putExtra(key, value)
    }

    override fun get(bundle: Bundle): DoubleArray? {
        return bundle.getDoubleArray(key) ?: value
    }
}