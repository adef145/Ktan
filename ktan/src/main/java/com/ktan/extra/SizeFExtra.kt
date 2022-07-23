package com.ktan.extra

import android.content.Intent
import android.os.Bundle
import android.util.SizeF

class SizeFExtra @JvmOverloads constructor(
    key: String,
    value: SizeF? = null
) : AbstractExtra<SizeF?>(
    key,
    value
) {
    override fun put(bundle: Bundle) {
        value?.also { bundle.putSizeF(key, it) }
    }

    override fun put(intent: Intent) {
        // do nothing
    }

    override fun get(bundle: Bundle): SizeF? {
        return bundle.getSizeF(key) ?: value
    }
}