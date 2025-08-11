package com.example.quickcast.SmsUtils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.quickcast.data_classes.SmsFormats.SmsPackage
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.GsonBuilder


/**
 * [SmsSenderWorker] is a [CoroutineWorker] which will do background work
 * of sending sms when user performs some action on application's UI.
 * */

class SmsSenderWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {

        // fetching json-format string containing message list.
        val jsonList = inputData.getString("messageList") ?: return Result.failure()

        val gson = GsonBuilder()
            .registerTypeAdapter(SmsPackage::class.java, SmsPackageDeserializer())
            .registerTypeAdapter(SmsPackage::class.java, SmsPackageSerializer())
            .create()
        val type = object : TypeToken<List<Pair<String, SmsPackage>>>() {}.type

        // converting json-format string back to List<SmsPackage>
        val messageList: List<Pair<String, SmsPackage>> = gson.fromJson(jsonList, type)

        val smsManager = SmsManager.getDefault()

        messageList.forEach { smsPackagePair ->

            // PendingIntent that will be broadcast when message is delivered successfully
            val deliveredIntent = PendingIntent.getBroadcast(
                applicationContext,
                0,
                Intent("SMS_DELIVERED"),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )


            // sending each message to the specific phone number contained in smsPackage with the message in json-formatted string
            // with a specified pending intent that will be broadcast when message is delivered successfully.
            smsManager.sendTextMessage(smsPackagePair.first, null, gson.toJson(smsPackagePair.second), null, deliveredIntent)

            Log.d("SMS_DELIVERY", "src : ${smsPackagePair.first}, content : ${smsPackagePair.second}")
        }

        return Result.success()
    }
}