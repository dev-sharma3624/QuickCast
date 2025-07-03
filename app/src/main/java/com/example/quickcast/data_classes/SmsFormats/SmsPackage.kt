package com.example.quickcast.data_classes.SmsFormats

import com.example.quickcast.enum_classes.SmsTypes

data class SmsPackage(
    val type : SmsTypes,
    val phone : String,
    val message: Any
)
