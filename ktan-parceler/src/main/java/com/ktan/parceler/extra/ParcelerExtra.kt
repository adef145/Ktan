package com.ktan.parceler.extra

import android.content.Intent
import android.os.Bundle
import com.ktan.extra.AbstractExtra
import org.parceler.Parcels

open class ParcelerExtra<T> @JvmOverloads constructor(
    key: String,
    value: T? = null
) : AbstractExtra<T?>(
    key,
    value
) {
    override fun put(bundle: Bundle) {
        bundle.putParcelable(key, Parcels.wrap(value))
    }

    override fun put(intent: Intent) {
        intent.putExtra(key, Parcels.wrap(value))
    }

    override fun get(bundle: Bundle): T {
        return Parcels.unwrap(bundle.getParcelable(key))
    }
}