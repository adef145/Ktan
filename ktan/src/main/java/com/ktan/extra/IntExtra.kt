package com.ktan.extra

import android.content.Intent
import android.os.Bundle

class IntExtra @JvmOverloads constructor(
    key: String,
    value: Int? = null
) : AbstractExtra<Int?>(
    key,
    value
) {
    override fun put(bundle: Bundle) {
        value?.also { bundle.putInt(key, it) }
    }

    override fun put(intent: Intent) {
        intent.putExtra(key, value)
    }

    override fun get(bundle: Bundle): Int {
        return value?.let { bundle.getInt(key, it) } ?: bundle.getInt(key)
    }
}