package com.example.quickcast.data_classes.SmsFormats

import com.example.quickcast.enum_classes.SmsTypes

/**
 * [SmsPackage] : represents the complete message that will be sent.
 * @param type defines what type of content the message contains.
 * @param phone contains the phone number of the sender.
 * @param message contains the message.
 * */
data class SmsPackage(
    val type : SmsTypes,
    val message: MessageContent
)
