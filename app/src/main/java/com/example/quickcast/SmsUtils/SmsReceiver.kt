package com.example.quickcast.SmsUtils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Telephony
import android.telephony.SmsMessage
import android.util.Log
import com.example.quickcast.data_classes.SmsFormats.SiteInvite
import com.example.quickcast.data_classes.SmsFormats.SmsPackage
import com.example.quickcast.enum_classes.SmsTypes
import com.google.gson.Gson
import com.google.gson.JsonObject

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val gson = Gson()
        Log.d("SmsReciever", "🔔 Receiver triggered")
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val bundle = intent.extras
            val format = bundle?.getString("format")
            if (bundle != null) {
                val pdus = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    bundle.getSerializable("pdus", Array<ByteArray>::class.java)
                } else {
                    bundle.get("pdus") as? Array<*>
                }

                if (pdus != null) {
                    for (pdu in pdus) {
                        try {
                            val sms = SmsMessage.createFromPdu(pdu as ByteArray, format)
                            val jsonMsg = gson.fromJson(sms.messageBody, SmsPackage::class.java)
                            Log.d("SmsReceiver", "received : $jsonMsg")
                            /*val sender = sms.displayOriginatingAddress
                            val body = sms.messageBody
                            Log.d("SmsReceiver", "✅ From: $sender, Message: $body")*/
                        } catch (e: Exception) {
                            Log.e("SmsReceiver", "❌ Error parsing SMS: ${e.message}")
                        }
                    }
                }else{
                    Log.d("SmsReceiver", "pdus is null")
                }
            }
        }
    }
}