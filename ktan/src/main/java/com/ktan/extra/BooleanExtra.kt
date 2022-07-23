package com.ktan.extra

import android.content.Intent
import android.os.Bundle

class BooleanExtra @JvmOverloads constructor(
    key: String,
    value: Boolean? = null
) : AbstractExtra<Boolean?>(
    key,
    value
) {
    override fun put(bundle: Bundle) {
        value?.also { bundle.putBoolean(key, it) }
    }

    override fun put(intent: Intent) {
        intent.putExtra(key, value)
    }

    override fun get(bundle: Bundle): Boolean {
        return value?.let { bundle.getBoolean(key, it) } ?: bundle.getBoolean(key)
    }
}