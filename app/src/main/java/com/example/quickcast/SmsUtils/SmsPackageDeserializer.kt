package com.example.quickcast.SmsUtils

import com.example.quickcast.data_classes.SmsFormats.InvitationResponse
import com.example.quickcast.data_classes.SmsFormats.MessageContent
import com.example.quickcast.data_classes.SmsFormats.SiteInvite
import com.example.quickcast.data_classes.SmsFormats.SmsPackage
import com.example.quickcast.enum_classes.SmsTypes
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type


/**
 * [SmsPackageDeserializer] is [JsonDeserializer] that allows custom transformation of messages to and from json formats.
 * Here we are trying to assert the base class of the message field of the [SmsPackage]
 * */

class SmsPackageDeserializer : JsonDeserializer<SmsPackage> {

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): SmsPackage {
        val jsonObject = json.asJsonObject

        val smsType = SmsTypes.valueOf(jsonObject["type"].asString)

        val messageJson = jsonObject["message"]
        val message: MessageContent = when (smsType) {
            SmsTypes.SITE_INVITE -> context.deserialize<SiteInvite>(messageJson, SiteInvite::class.java)
            SmsTypes.INVITATION_RESPONSE -> context.deserialize<InvitationResponse>(messageJson, InvitationResponse::class.java)
        }

        return SmsPackage(smsType, message)
    }
}