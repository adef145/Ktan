package com.ktan.router

import android.content.Intent
import android.os.Bundle
import com.ktan.Ktan
import com.ktan.extra.Extra

open class KtanRouter : Ktan {

    private val extraList: MutableList<Extra<*>> by lazy { mutableListOf() }

    fun populate(intent: Intent): Intent {
        extraList.forEach {
            it.put(intent)
        }
        return intent
    }

    fun populate(bundle: Bundle): Bundle {
        extraList.forEach {
            it.put(bundle)
        }
        return bundle
    }

    override fun onInitiateExtraProperty(extra: Extra<*>) {
        extraList.add(extra)
    }
}