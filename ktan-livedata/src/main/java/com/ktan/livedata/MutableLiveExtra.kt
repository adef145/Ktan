package com.ktan.livedata

import androidx.lifecycle.MutableLiveData
import com.ktan.extra.MutableExtra

open class MutableLiveExtra<T>(
    private val extra: MutableExtra<T?>,
    initialValue: T?
) : MutableLiveData<T>(initialValue) {

    override fun setValue(value: T) {
        super.setValue(value)
        extra.set(value)
    }
}