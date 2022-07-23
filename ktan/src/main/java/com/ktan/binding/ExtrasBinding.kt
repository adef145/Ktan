package com.ktan.binding

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ktan.Ktan
import com.ktan.extra.Extra

open class ExtrasBinding : Ktan {

    private val extraList: MutableList<Extra<*>> by lazy { mutableListOf() }

    private val savedExtraList: MutableList<Extra<*>> by lazy { mutableListOf() }

    fun bind(vararg bundles: Bundle?) {
        extraList.forEach {
            it.bind(*bundles)
        }
    }

    fun saveInstanceState(outState: Bundle) {
        savedExtraList.forEach {
            it.put(outState)
        }
    }

    override fun onInitiateExtraProperty(extra: Extra<*>) {
        extraList.add(extra)
        if (!savedExtraList.contains(extra)) {
            savedExtraList.add(extra)
        }
    }
}

inline fun <reified T : ExtrasBinding> Activity.bindExtras(): Lazy<T> =
    lazy {
        T::class.java.newInstance().also {
            bindExtras(this, it)
        }
    }

inline fun <reified T : ExtrasBinding> Fragment.bindExtras(): T =
    T::class.java.newInstance().also {
        bindExtras(this, it)
    }

inline fun <reified T : ExtrasBinding> Intent.bindExtras(): T =
    bindExtras(this, T::class.java.newInstance())

inline fun <reified T : ExtrasBinding> Bundle.bindExtras(): T =
    bindExtras(this, T::class.java.newInstance())

fun bindExtras(activity: Activity, binding: ExtrasBinding) {
    bindExtras(activity, binding, activity.intent.extras)
}

fun bindExtras(fragment: Fragment, binding: ExtrasBinding) {
    bindExtras(fragment, binding, fragment.arguments)
}

fun <T : ExtrasBinding> bindExtras(intent: Intent?, binding: T): T {
    binding.bind(intent?.extras)
    return binding
}

fun <T : ExtrasBinding> bindExtras(bundle: Bundle?, binding: T): T {
    binding.bind(bundle)
    return binding
}

fun bindExtras(obj: Any, binding: ExtrasBinding, bundle: Bundle?) {
    binding.bind(Ktan.getSavedInstance(obj), bundle)
    Ktan.registerBinding(obj, binding)
}