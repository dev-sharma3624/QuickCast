package com.example.quickcast.room_db.background_workers

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.quickcast.MainActivity
import com.example.quickcast.repositories.DatabaseRepository
import com.example.quickcast.services.NotificationService

class CreateTaskBgWorker(
    appContext : Context,
    params : WorkerParameters,
    private val dbRepo : DatabaseRepository
) : CoroutineWorker(appContext, params) {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override suspend fun doWork(): Result {

        val siteId = inputData.getLong("site_Id", 0L)
        val isAppInForeground = inputData.getBoolean("is_App_In_Foreground", false)
        val task = inputData.getString("task") ?: ""
        val phoneNumber = inputData.getString("phone_Number") ?: ""

        if(siteId == 0L){
            return Result.failure()
        }else{

            val site = dbRepo.getSiteFromId(siteId)

            val notificationService = NotificationService()

            val intent = Intent(
                applicationContext,
                MainActivity::class.java
            )
            intent.apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }

            val pendingIntent = PendingIntent.getActivity(
                applicationContext,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            notificationService.showSimpleNotification(
                context = applicationContext,
                msg = "New task received in ${site.name}",
                pendingIntent = if(!isAppInForeground){ // if (app in foreground) pendingIntent else null
                    pendingIntent
                } else null
            )

            dbRepo.messagePropertyAddition(siteId, task, phoneNumber)


            return  Result.success()
        }

    }
}