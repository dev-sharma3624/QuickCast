package com.example.quickcast.data_classes

import com.example.quickcast.data_classes.SmsFormats.SendableMessageProperty
import com.example.quickcast.enum_classes.SmsTypes

data class MessageGraph(
    val msgId: Int,
    val sentBy: String,
    val smsType: SmsTypes,
    val content: SendableMessageProperty
)
