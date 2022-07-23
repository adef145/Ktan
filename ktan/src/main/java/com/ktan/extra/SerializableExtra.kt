package com.ktan.extra

import android.content.Intent
import android.os.Bundle
import java.io.Serializable

class SerializableExtra @JvmOverloads constructor(
    key: String,
    value: Serializable? = null
) : AbstractExtra<Serializable?>(
    key,
    value
) {
    override fun put(bundle: Bundle) {
        value?.also { bundle.putSerializable(key, it) }
    }

    override fun put(intent: Intent) {
        intent.putExtra(key, value)
    }

    override fun get(bundle: Bundle): Serializable? {
        return bundle.getSerializable(key) ?: value
    }
}