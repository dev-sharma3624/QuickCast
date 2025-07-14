package com.example.quickcast.SmsUtils

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Telephony
import android.telephony.SmsMessage
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.quickcast.MainActivity
import com.example.quickcast.data_classes.SmsFormats.SiteInvite
import com.example.quickcast.data_classes.SmsFormats.SmsPackage
import com.example.quickcast.enum_classes.SmsTypes
import com.example.quickcast.room_db.entities.Site
import com.example.quickcast.services.NotificationService
import com.google.gson.Gson
import com.google.gson.GsonBuilder

/**
 * [SmsReceiver] is a broadcast receiver which reads incoming messages which we can process
 * as desired
 * */

class SmsReceiver : BroadcastReceiver() {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onReceive(context: Context, intent: Intent) {
        val gson = GsonBuilder()
            .registerTypeAdapter(SmsPackage::class.java, SmsPackageDeserializer())
            .registerTypeAdapter(SmsPackage::class.java, SmsPackageDeserializer())
            .create()

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

                            decideSuitableAction(jsonMsg, context)


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

    /**
     * [decideSuitableAction] function to decide the suitable action based on the type of message.
     * */

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun decideSuitableAction(receivedMsg : SmsPackage, context: Context){

        when(receivedMsg.type){

            SmsTypes.SITE_INVITE -> siteInviteProcess(context)
        }


    }

    /**
     * [createPendingIntent] creates a [PendingIntent] to be passed to notification which will launched when the user
     * taps on notification.
     * */

    private fun createPendingIntent(context: Context, smsTypes: SmsTypes) : PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        when(smsTypes){
            SmsTypes.SITE_INVITE -> {
                intent.putExtra(smsTypes.name, true)
            }
        }


        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return pendingIntent

    }

    /**
     *[siteInviteProcess] defines the process that needs to executed when the type of message received is a
     * [SmsTypes.SITE_INVITE]. It includes:
     *
     * -> showing a notification either with a null pending intent or a non-null pending intent based on whether
     * the app is currently in foreground or background respectively.
     * */

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun siteInviteProcess(context : Context){

        // checks whether app is in foreground or not
        val isAppInForeground = ProcessLifecycleOwner.get().lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)

        if(isAppInForeground){
            // launches an intent that will be received by onNewIntent function in MainActivity
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_SINGLE_TOP
                putExtra(SmsTypes.SITE_INVITE.name, true)
            }
            context.startActivity(intent)

        }

        NotificationService().showSimpleNotification(
            context = context,
            msg = "You have received a new site invitation.",
            pendingIntent = if(!isAppInForeground){ // if (app in foreground) pendingIntent else null
                createPendingIntent(
                    context = context,
                    smsTypes = SmsTypes.SITE_INVITE
                )
            } else null
        )
    }
}
























