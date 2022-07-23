package com.ktan.extra

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable

class ParcelableArrayExtra @JvmOverloads constructor(
    key: String,
    value: Array<out Parcelable>? = null
) : AbstractExtra<Array<out Parcelable>?>(
    key,
    value
) {
    override fun put(bundle: Bundle) {
        value?.also { bundle.putParcelableArray(key, it) }
    }

    override fun put(intent: Intent) {
        intent.putExtra(key, value)
    }

    override fun get(bundle: Bundle): Array<out Parcelable>? {
        return bundle.getParcelableArray(key) ?: value
    }
}