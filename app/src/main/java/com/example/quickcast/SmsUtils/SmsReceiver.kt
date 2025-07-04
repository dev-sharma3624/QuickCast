package com.example.quickcast.SmsUtils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Telephony
import android.telephony.SmsMessage
import android.util.Log
import com.example.quickcast.data_classes.SmsFormats.SmsPackage
import com.google.gson.Gson

/**
 * [SmsReceiver] is a broadcast receiver which reads incoming messages which we can process
 * as desired
 * */

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val gson = Gson()
        Log.d("SmsReciever", "🔔 Receiver triggered")

        //double checks the action of intent that triggered receiver.
        // Important because "exported = true" is manifest for this receiver
        // which is needed to list to system level broadcasts like SMS_RECEIVED
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {


            val bundle = intent.extras
            val format = bundle?.getString("format")

            //this check is needed because bundle is of type Bundle? so it
            // behaves like let block and provides a safe null-safety block
            if (bundle != null) {

                //pdus(Protocol data units) is an array containing raw binary data of
                // sms message. Pdu is the format in which messages are transmitted.
                // -------------------------------------------------------------------------------------
                // If-else block ensures future proofing as get() method is deprecated
                // for api 33+
                val pdus = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    bundle.getSerializable("pdus", Array<ByteArray>::class.java)
                } else {
                    bundle.get("pdus") as? Array<*>
                }

                //this check is needed because pdus is of type Array<out Any?>? so it
                // behaves like let block and provides a safe null-safety block
                if (pdus != null) {

                    // for messages < 170 characters pdus array will contain only one
                    // element and hence loop will run only once but in case in future
                    // if message length increases loop will run multiple times because
                    // message will be sent in multiple parts and hence we will need an
                    // extra variable of type string where we will keep appending the sms
                    // variable content.
                    for (pdu in pdus) {
                        try {

                            //SmsMessage type object that contains the message in string format
                            val sms = SmsMessage.createFromPdu(pdu as ByteArray, format)

                            //creates an object of type SmsPackage from sms -> messageBody.
                            val jsonMsg = gson.fromJson(sms.messageBody, SmsPackage::class.java)
                            Log.d("SmsReceiver", "received : $jsonMsg")

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