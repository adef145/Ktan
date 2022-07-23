package com.ktan.extra

import android.content.Intent
import android.os.Bundle

class BundleExtra @JvmOverloads constructor(
    key: String,
    value: Bundle? = null
) : AbstractExtra<Bundle?>(
    key,
    value
) {
    override fun put(bundle: Bundle) {
        value?.also { bundle.putBundle(key, it) }
    }

    override fun put(intent: Intent) {
        intent.putExtra(key, value)
    }

    override fun get(bundle: Bundle): Bundle? {
        return bundle.getBundle(key) ?: value
    }
}