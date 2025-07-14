package com.example.quickcast.room_db.dto

import com.example.quickcast.enum_classes.SmsTypes

data class MessageDTO(
    val msgId: Int,
    val sentBy: String,
    val smsType: SmsTypes,
    val content: String
)
