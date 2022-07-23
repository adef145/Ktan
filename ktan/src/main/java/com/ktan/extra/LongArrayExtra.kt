package com.ktan.extra

import android.content.Intent
import android.os.Bundle

class LongArrayExtra @JvmOverloads constructor(
    key: String,
    value: LongArray? = null
) : AbstractExtra<LongArray?>(
    key,
    value
) {
    override fun put(bundle: Bundle) {
        value?.also { bundle.putLongArray(key, it) }
    }

    override fun put(intent: Intent) {
        intent.putExtra(key, value)
    }

    override fun get(bundle: Bundle): LongArray? {
        return bundle.getLongArray(key) ?: value
    }
}