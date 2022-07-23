package com.ktan.extra

import android.content.Intent
import android.os.Bundle

class ByteExtra @JvmOverloads constructor(
    key: String,
    value: Byte? = null
) : AbstractExtra<Byte?>(
    key,
    value
) {
    override fun put(bundle: Bundle) {
        value?.also { bundle.putByte(key, it) }
    }

    override fun put(intent: Intent) {
        intent.putExtra(key, value)
    }

    override fun get(bundle: Bundle): Byte {
        return value?.let { bundle.getByte(key, it) } ?: bundle.getByte(key)
    }
}