package com.ktan.extra

import android.content.Intent
import android.os.Bundle

class StringExtra @JvmOverloads constructor(
    key: String,
    value: String? = null
) : AbstractExtra<String?>(
    key,
    value
) {
    override fun put(bundle: Bundle) {
        bundle.putString(key, value)
    }

    override fun put(intent: Intent) {
        intent.putExtra(key, value)
    }

    override fun get(bundle: Bundle): String? {
        return bundle.getString(key) ?: value
    }
}