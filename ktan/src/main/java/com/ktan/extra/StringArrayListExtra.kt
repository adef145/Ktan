package com.ktan.extra

import android.content.Intent
import android.os.Bundle

class StringArrayListExtra @JvmOverloads constructor(
    key: String,
    value: ArrayList<String>? = null
) : AbstractExtra<ArrayList<String>?>(
    key,
    value
) {
    override fun put(bundle: Bundle) {
        bundle.putStringArrayList(key, value)
    }

    override fun put(intent: Intent) {
        intent.putExtra(key, value)
    }

    override fun get(bundle: Bundle): ArrayList<String>? {
        return bundle.getStringArrayList(key) ?: value
    }
}