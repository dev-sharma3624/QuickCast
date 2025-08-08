package com.example.quickcast.repositories

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.quickcast.SmsUtils.SmsPackageDeserializer
import com.example.quickcast.SmsUtils.SmsPackageSerializer
import com.example.quickcast.SmsUtils.SmsSenderWorker
import com.example.quickcast.data_classes.SmsFormats.SmsPackage
import com.google.gson.Gson
import com.google.gson.GsonBuilder

/**
 * [SmsRepository] contains implementation of sending and receiving SMS to/from other devices respectively.
 * @param context required for scheduling sms sending
 * */

class SmsRepository(
    private val context : Context,
    private val gson : Gson = GsonBuilder()
        .registerTypeAdapter(SmsPackage::class.java, SmsPackageDeserializer())
        .registerTypeAdapter(SmsPackage::class.java, SmsPackageSerializer())
        .create()
) {

    /**
     * [sendMessage] receives a list of type [SmsPackage] which then sends it to various devices through [SmsSenderWorker]
     * */
    fun sendMessage(smsList : List<SmsPackage>): String?{

        try {
            //converts smsList to json format
            val jsonMsgList = gson.toJson(smsList)

            //converts jsonMsgList as Data type object
            val inputData = workDataOf(
                "messageList" to jsonMsgList
            )

            //generates one time work request with input data for SmsSenderWorker
            val request = OneTimeWorkRequestBuilder<SmsSenderWorker>()
                .setInputData(inputData)
                .build()

            //enqueues request for background processing
            WorkManager.getInstance(context).enqueue(request)

            return null
        }catch (e : Exception){
            return e.message
        }


    }


}