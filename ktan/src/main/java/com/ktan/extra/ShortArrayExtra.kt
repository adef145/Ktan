package com.ktan.extra

import android.content.Intent
import android.os.Bundle

class ShortArrayExtra @JvmOverloads constructor(
    key: String,
    value: ShortArray? = null
) : AbstractExtra<ShortArray?>(
    key,
    value
) {
    override fun put(bundle: Bundle) {
        value?.also { bundle.putShortArray(key, it) }
    }

    override fun put(intent: Intent) {
        intent.putExtra(key, value)
    }

    override fun get(bundle: Bundle): ShortArray? {
        return bundle.getShortArray(key) ?: value
    }
}