package com.example.quickcast

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class QuickCast : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@QuickCast)
        }

    }
}