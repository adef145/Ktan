package com.ktan.extra

interface MutableExtra<T> : Extra<T> {

    fun set(value: T)
}