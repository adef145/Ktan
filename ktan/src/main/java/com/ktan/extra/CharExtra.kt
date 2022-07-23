package com.ktan.extra

import android.content.Intent
import android.os.Bundle

class CharExtra @JvmOverloads constructor(
    key: String,
    value: Char? = null
) : AbstractExtra<Char?>(
    key,
    value
) {
    override fun put(bundle: Bundle) {
        value?.also { bundle.putChar(key, it) }
    }

    override fun put(intent: Intent) {
        intent.putExtra(key, value)
    }

    override fun get(bundle: Bundle): Char {
        return value?.let { bundle.getChar(key, it) } ?: bundle.getChar(key)
    }
}