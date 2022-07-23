package com.ktan.extra

import android.content.Intent
import android.os.Bundle

class ByteArrayExtra @JvmOverloads constructor(
    key: String,
    value: ByteArray? = null
) : AbstractExtra<ByteArray?>(
    key,
    value
) {
    override fun put(bundle: Bundle) {
        value?.also { bundle.putByteArray(key, it) }
    }

    override fun put(intent: Intent) {
        intent.putExtra(key, value)
    }

    override fun get(bundle: Bundle): ByteArray? {
        return bundle.getByteArray(key) ?: value
    }
}