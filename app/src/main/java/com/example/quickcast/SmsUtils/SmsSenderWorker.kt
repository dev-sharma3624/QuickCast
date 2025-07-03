package com.example.quickcast.SmsUtils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.quickcast.data_classes.SmsFormats.SiteInvite
import com.example.quickcast.data_classes.SmsFormats.SmsPackage
import com.example.quickcast.enum_classes.SmsTypes
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson

class SmsSenderWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {

        val jsonList = inputData.getString("messageList") ?: return Result.failure()

        val gson = Gson()
        val type = object : TypeToken<List<SmsPackage>>() {}.type
        val messageList: List<SmsPackage> = gson.fromJson(jsonList, type)

        val smsManager = SmsManager.getDefault()

        messageList.forEach { smsPackage ->
            val deliveredIntent = PendingIntent.getBroadcast(
                applicationContext,
                0,
                Intent("SMS_DELIVERED"),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )


            smsManager.sendTextMessage(smsPackage.phone, null, gson.toJson(smsPackage), null, deliveredIntent)

            Log.d("SMS_DELIVERY", "src : ${smsPackage.phone}, content : ${smsPackage}")
        }

        return Result.success()
    }
}