package com.ktan.processor

interface KtanBuilder<T> {

    fun build(): T
}