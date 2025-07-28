package com.example.quickcast.data_classes

import com.example.quickcast.enum_classes.MessagePropertyTypes

data class MessageProperties(
    val id : Int,
    val name : String,
    val value : Int,
    val type : MessagePropertyTypes
)
