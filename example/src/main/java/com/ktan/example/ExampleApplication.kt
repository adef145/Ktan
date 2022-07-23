package com.ktan.example

import android.app.Application
import com.ktan.Ktan

class ExampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Ktan.init(this)
    }
}