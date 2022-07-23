package com.ktan.extra

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable

class ParcelableExtra @JvmOverloads constructor(
    key: String,
    value: Parcelable? = null
) : AbstractExtra<Parcelable?>(
    key,
    value
) {
    override fun put(bundle: Bundle) {
        value?.also { bundle.putParcelable(key, it) }
    }

    override fun put(intent: Intent) {
        intent.putExtra(key, value)
    }

    override fun get(bundle: Bundle): Parcelable? {
        return bundle.getParcelable(key) ?: value
    }
}