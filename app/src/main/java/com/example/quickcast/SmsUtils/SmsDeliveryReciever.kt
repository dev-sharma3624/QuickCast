package com.example.quickcast.SmsUtils

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * [SmsDeliveryReceiver] is a Broadcast receiver from where delivery status
 * of each message sent will be tracked. We can then perform actions based
 *  on deliver status.
 *  For e.g. : If any message delivery causes an error then we can execute
 *  rollback command to keep the database consistent amongst all devices.
 * */

class SmsDeliveryReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        //resultCode is the variable that stores the value of
        // result received for the specified intent in intent filter
        when (resultCode) {

            //sms delivered without any error successfully
            Activity.RESULT_OK -> {
                Log.d("SmsDelivery", "✅ SMS delivered")
            }

            //some error encountered
            else -> {
                Log.d("SmsDelivery", "❌ SMS NOT delivered")
            }
        }
    }
}