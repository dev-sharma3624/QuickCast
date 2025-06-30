package com.example.quickcast

import android.app.Application
import com.example.quickcast.viewModels.HomeVM
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

class QuickCast : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@QuickCast)
            modules(module {
                viewModel { HomeVM() }
            })
        }

    }
}