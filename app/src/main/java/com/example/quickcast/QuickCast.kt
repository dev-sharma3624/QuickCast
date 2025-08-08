package com.example.quickcast

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.room.Room
import com.example.quickcast.repositories.DatabaseRepository
import com.example.quickcast.repositories.SmsRepository
import com.example.quickcast.room_db.AppDb
import com.example.quickcast.services.NotificationService
import com.example.quickcast.viewModels.HomeVM
import com.example.quickcast.viewModels.SiteScreenVM
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

class QuickCast : Application() {
    override fun onCreate() {
        super.onCreate()

        // needed to track lifecycle of application like whether app is in foreground, background etc.
        ProcessLifecycleOwner.get().lifecycle

        NotificationService().createNotificationChannel(this)

        val databaseModule = module {

            single {
                Room.databaseBuilder(
                    this@QuickCast,
                    AppDb::class.java,
                    "App_Db"
                ).build()
            }

            single { get<AppDb>().siteDao() }
            single { get<AppDb>().messageDao() }

            single { DatabaseRepository(get(), get()) }
            single { SmsRepository(this@QuickCast) }
        }


        startKoin {
            androidContext(this@QuickCast)
            modules(
                databaseModule,
                module { viewModel { HomeVM(get(), get()) } },
                module { viewModel { SiteScreenVM(get()) } }
            )
        }

    }
}