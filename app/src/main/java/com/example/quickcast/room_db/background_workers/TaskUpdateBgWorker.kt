package com.example.quickcast.room_db.background_workers

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.quickcast.MainActivity
import com.example.quickcast.data_classes.SmsFormats.TaskUpdate
import com.example.quickcast.repositories.DatabaseRepository
import com.example.quickcast.services.NotificationService
import com.google.gson.Gson

class TaskUpdateBgWorker(
    appContext : Context,
    params : WorkerParameters,
    private val dbRepo : DatabaseRepository
) : CoroutineWorker(appContext, params) {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override suspend fun doWork(): Result {

        /*val inputData = workDataOf(
            "format_Id" to taskUpdate.fId,
            "is_App_In_Foreground" to isAppInForeground(),
            "task_Update" to gson.toJson(taskUpdate),
            "phone_Number" to phoneNumber
        )*/

        val formatId = inputData.getLong("format_Id", 0L)
        val isAppInForeground = inputData.getBoolean("is_App_In_Foreground", false)
        val taskUpdateString = inputData.getString("task_Update") ?: ""
        val phoneNumber = inputData.getString("phone_Number") ?: ""

        if(formatId == 0L){
            return Result.failure()
        }else{

            val siteId = dbRepo.getSiteIdFromFormatId(formatId)

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
                msg = "New update received",
                pendingIntent = if(!isAppInForeground){ // if (app in foreground) pendingIntent else null
                    pendingIntent
                } else null
            )

            dbRepo.sendUpdateMessage(
                siteId = siteId,
                taskUpdate = Gson().fromJson(taskUpdateString, TaskUpdate::class.java),
                phoneNumber = phoneNumber
            )


            return  Result.success()
        }

    }
}
