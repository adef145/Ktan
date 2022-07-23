package com.ktan.extra

import android.content.Intent
import android.os.Bundle

class ShortExtra @JvmOverloads constructor(
    key: String,
    value: Short? = null
) : AbstractExtra<Short?>(
    key,
    value
) {
    override fun put(bundle: Bundle) {
        value?.also { bundle.putShort(key, it) }
    }

    override fun put(intent: Intent) {
        intent.putExtra(key, value)
    }

    override fun get(bundle: Bundle): Short {
        return value?.let { bundle.getShort(key, it) } ?: bundle.getShort(key)
    }
}