package com.ktan.extra

import android.content.Intent
import android.os.Bundle

interface Extra<T> {

    fun get(): T?

    fun bind(vararg bundles: Bundle?)

    fun put(bundle: Bundle)

    fun put(intent: Intent)
}