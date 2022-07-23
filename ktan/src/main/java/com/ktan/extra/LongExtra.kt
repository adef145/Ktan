package com.ktan.extra

import android.content.Intent
import android.os.Bundle

class LongExtra @JvmOverloads constructor(
    key: String,
    value: Long? = null
) : AbstractExtra<Long?>(
    key,
    value
) {
    override fun put(bundle: Bundle) {
        value?.also { bundle.putLong(key, it) }
    }

    override fun put(intent: Intent) {
        intent.putExtra(key, value)
    }

    override fun get(bundle: Bundle): Long {
        return value?.let { bundle.getLong(key, it) } ?: bundle.getLong(key)
    }
}