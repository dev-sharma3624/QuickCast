package com.example.quickcast.SmsUtils

import com.example.quickcast.data_classes.SmsFormats.SiteInvite
import com.example.quickcast.data_classes.SmsFormats.SmsPackage
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class SmsPackageSerializer : JsonSerializer<SmsPackage> {
    override fun serialize(
        src: SmsPackage,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        val obj = JsonObject()
        obj.addProperty("type", src.type.name)
        obj.addProperty("phone", src.phone)

        val messageJson = when (val msg = src.message) {
            is SiteInvite -> context.serialize(msg)
        }

        obj.add("message", messageJson)
        return obj
    }
}