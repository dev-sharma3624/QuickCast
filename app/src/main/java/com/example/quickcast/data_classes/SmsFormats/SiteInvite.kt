package com.example.quickcast.data_classes.SmsFormats

import com.example.quickcast.enum_classes.SmsTypes

data class SiteInvite (
    val n : String, //site name
    val l : List<String>? //list of all other contacts
)
