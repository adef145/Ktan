package com.ktan.extra

import android.content.Intent
import android.os.Bundle
import android.util.Size

class SizeExtra @JvmOverloads constructor(
    key: String,
    value: Size? = null
) : AbstractExtra<Size?>(
    key,
    value
) {
    override fun put(bundle: Bundle) {
        value?.also { bundle.putSize(key, it) }
    }

    override fun put(intent: Intent) {
        // do nothing
    }

    override fun get(bundle: Bundle): Size? {
        return bundle.getSize(key) ?: value
    }
}