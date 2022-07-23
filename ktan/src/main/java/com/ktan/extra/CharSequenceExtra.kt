package com.ktan.extra

import android.content.Intent
import android.os.Bundle

class CharSequenceExtra @JvmOverloads constructor(
    key: String,
    value: CharSequence? = null
) : AbstractExtra<CharSequence?>(
    key,
    value
) {
    override fun put(bundle: Bundle) {
        value?.also { bundle.putCharSequence(key, it) }
    }

    override fun put(intent: Intent) {
        intent.putExtra(key, value)
    }

    override fun get(bundle: Bundle): CharSequence? {
        return value?.let { bundle.getCharSequence(key, it) } ?: bundle.getCharSequence(key)
    }
}