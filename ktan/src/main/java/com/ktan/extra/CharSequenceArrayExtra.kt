package com.ktan.extra

import android.content.Intent
import android.os.Bundle

class CharSequenceArrayExtra @JvmOverloads constructor(
    key: String,
    value: Array<out CharSequence>? = null
) : AbstractExtra<Array<out CharSequence>?>(
    key,
    value
) {
    override fun put(bundle: Bundle) {
        value?.also { bundle.putCharSequenceArray(key, it) }
    }

    override fun put(intent: Intent) {
        intent.putExtra(key, value)
    }

    override fun get(bundle: Bundle): Array<out CharSequence>? {
        return bundle.getCharSequenceArray(key) ?: value
    }
}