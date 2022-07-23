package com.ktan.extra

import android.content.Intent
import android.os.Bundle

class IntArrayExtra @JvmOverloads constructor(
    key: String,
    value: IntArray? = null
) : AbstractExtra<IntArray?>(
    key,
    value
) {
    override fun put(bundle: Bundle) {
        value?.also { bundle.putIntArray(key, it) }
    }

    override fun put(intent: Intent) {
        intent.putExtra(key, value)
    }

    override fun get(bundle: Bundle): IntArray? {
        return bundle.getIntArray(key) ?: value
    }
}