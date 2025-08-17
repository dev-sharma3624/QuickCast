package com.example.quickcast.SmsUtils

import android.Manifest
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
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.quickcast.MainActivity
import com.example.quickcast.data_classes.SmsFormats.InvitationResponse
import com.example.quickcast.data_classes.SmsFormats.CreateTask
import com.example.quickcast.data_classes.SmsFormats.MessageContent
import com.example.quickcast.data_classes.SmsFormats.SiteInvite
import com.example.quickcast.data_classes.SmsFormats.SmsPackage
import com.example.quickcast.enum_classes.SmsTypes
import com.example.quickcast.room_db.background_workers.CreateTaskBgWorker
import com.example.quickcast.room_db.background_workers.InvitationResponseBgWorker
import com.example.quickcast.services.ContactsService
import com.example.quickcast.services.NotificationService
import com.google.gson.GsonBuilder

/**
 * [SmsReceiver] is a broadcast receiver which reads incoming messages which we can process
 * as desired
 * */

class SmsReceiver : BroadcastReceiver() {

    private val gson = GsonBuilder()
        .registerTypeAdapter(SmsPackage::class.java, SmsPackageDeserializer())
        .registerTypeAdapter(SmsPackage::class.java, SmsPackageSerializer())
        .create()

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onReceive(context: Context, intent: Intent) {


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

                            decideSuitableAction(jsonMsg, sms.displayOriginatingAddress, context)


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
    private fun decideSuitableAction(receivedMsg: SmsPackage, phoneNumber: String, context: Context){

        when(receivedMsg.message){

            is SiteInvite -> siteInviteProcess(context, phoneNumber, receivedMsg.message)
            is InvitationResponse -> inviteResponse(context, phoneNumber, receivedMsg.message)
            is CreateTask -> {
                // Todo: new task received notification, insertion to db, changing unread status
                createTaskResponse(context, receivedMsg.message.siteId, receivedMsg.message, phoneNumber)
            }
        }


    }

    private fun createTaskResponse(
        context: Context,
        siteId: Long,
        task: CreateTask,
        phoneNumber: String
    ) {

        Log.d("json_format_list", gson.toJson(task))

        val inputData = workDataOf(
            "site_Id" to siteId,
            "is_App_In_Foreground" to isAppInForeground(),
            "task" to gson.toJson(task),
            "phone_Number" to phoneNumber
        )

        //generates one time work request with input data for SmsSenderWorker
        val request = OneTimeWorkRequestBuilder<CreateTaskBgWorker>()
            .setInputData(inputData)
            .build()

        //enqueues request for background processing
        WorkManager.getInstance(context).enqueue(request)

    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun inviteResponse(context: Context, phoneNumber: String, receivedMsg: InvitationResponse) {

        val contactsList = ContactsService().getContactsList(context)

        val isAppInForeground = isAppInForeground()

        val contactName = contactsList.find { it.number == phoneNumber }?.name ?: phoneNumber

        val response = if(receivedMsg.b) "accepted" else "declined"


        val inputData = workDataOf(
            "site_Id" to receivedMsg.id,
            "response" to receivedMsg.b,
            "phone_Number" to phoneNumber,
            "content_String" to "$phoneNumber $response your invitation."
        )

        //generates one time work request with input data for SmsSenderWorker
        val request = OneTimeWorkRequestBuilder<InvitationResponseBgWorker>()
            .setInputData(inputData)
            .build()

        //enqueues request for background processing
        WorkManager.getInstance(context).enqueue(request)

        val notificationService = NotificationService()

        notificationService.showSimpleNotification(
            context = context,
            msg = "$contactName $response your invitation",
            pendingIntent = if(!isAppInForeground){ // if (app in foreground) pendingIntent else null
                notificationService.createPendingIntent(
                    context = context,
                    phoneNumber = phoneNumber,
                    messageContent = receivedMsg
                )
            } else null
        )

    }

    /**
     *[siteInviteProcess] defines the process that needs to executed when the type of message received is a
     * [SmsTypes.SITE_INVITE]. It includes:
     *
     * -> showing a notification either with a null pending intent or a non-null pending intent based on whether
     * the app is currently in foreground or background respectively.
     * */

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun siteInviteProcess(
        context: Context,
        phoneNumber: String,
        messageContent: MessageContent
    ){

        // checks whether app is in foreground or not
        val isAppInForeground = isAppInForeground()

        if(isAppInForeground){
            // launches an intent that will be received by onNewIntent function in MainActivity
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_SINGLE_TOP
                putExtra("Msg_Object", messageContent as SiteInvite)
                putExtra("Phone_Number", phoneNumber)
            }
            context.startActivity(intent)

        }

        val notificationService = NotificationService()

        notificationService.showSimpleNotification(
            context = context,
            msg = "You have received a new site invitation.",
            pendingIntent = if(!isAppInForeground){ // if (app in foreground) pendingIntent else null
                notificationService.createPendingIntent(
                    context = context,
                    phoneNumber = phoneNumber,
                    messageContent = messageContent as SiteInvite
                )
            } else null
        )
    }

    private fun isAppInForeground() : Boolean{

        // checks whether app is in foreground or not
        return ProcessLifecycleOwner.get().lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)
    }
}
























