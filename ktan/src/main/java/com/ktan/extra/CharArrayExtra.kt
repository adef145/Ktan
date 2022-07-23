package com.ktan.extra

import android.content.Intent
import android.os.Bundle

class CharArrayExtra @JvmOverloads constructor(
    key: String,
    value: CharArray? = null
) : AbstractExtra<CharArray?>(
    key,
    value
) {
    override fun put(bundle: Bundle) {
        value?.also { bundle.putCharArray(key, it) }
    }

    override fun put(intent: Intent) {
        intent.putExtra(key, value)
    }

    override fun get(bundle: Bundle): CharArray? {
        return bundle.getCharArray(key) ?: value
    }
}