package com.ktan.extra

import android.content.Intent
import android.os.Bundle

class StringArrayExtra @JvmOverloads constructor(
    key: String,
    value: Array<out String>? = null
) : AbstractExtra<Array<out String>?>(
    key,
    value
) {
    override fun put(bundle: Bundle) {
        bundle.putStringArray(key, value)
    }

    override fun put(intent: Intent) {
        intent.putExtra(key, value)
    }

    override fun get(bundle: Bundle): Array<out String>? {
        return bundle.getStringArray(key) ?: value
    }
}