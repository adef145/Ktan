package com.ktan.extra

import android.content.Intent
import android.os.Bundle
import android.os.IBinder

class BinderExtra @JvmOverloads constructor(
    key: String,
    value: IBinder? = null
) : AbstractExtra<IBinder?>(
    key,
    value
) {
    override fun put(bundle: Bundle) {
        value?.also { bundle.putBinder(key, it) }
    }

    override fun put(intent: Intent) {
        // do nothing
    }

    override fun get(bundle: Bundle): IBinder? {
        return bundle.getBinder(key) ?: value
    }
}