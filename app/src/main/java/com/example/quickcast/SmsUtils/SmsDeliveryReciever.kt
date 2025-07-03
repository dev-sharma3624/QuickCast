package com.example.quickcast.SmsUtils

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class SmsDeliveryReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                Log.d("SmsDelivery", "✅ SMS delivered")
            }
            else -> {
                Log.d("SmsDelivery", "❌ SMS NOT delivered")
            }
        }
    }
}