package com.example.quickcast

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.room.Room
import com.example.quickcast.repositories.DatabaseRepository
import com.example.quickcast.repositories.SmsRepository
import com.example.quickcast.room_db.AppDb
import com.example.quickcast.room_db.background_workers.CreateTaskBgWorker
import com.example.quickcast.room_db.background_workers.InvitationResponseBgWorker
import com.example.quickcast.services.NotificationService
import com.example.quickcast.viewModels.HomeVM
import com.example.quickcast.viewModels.SiteScreenVM
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.worker
import org.koin.androidx.workmanager.koin.workManagerFactory
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
            single { get<AppDb>().formatDao() }

            single { DatabaseRepository(get(), get(), get()) }
            single { SmsRepository(this@QuickCast) }

            worker { InvitationResponseBgWorker(androidContext(), get(), get()) }
            worker { CreateTaskBgWorker(androidContext(), get(), get()) }

        }


        startKoin {
            androidContext(this@QuickCast)
            workManagerFactory()
            modules(
                databaseModule,
                module { viewModel { HomeVM(get(), get()) } },
                module { viewModel { SiteScreenVM(get(), get()) } }
            )
        }

    }
}