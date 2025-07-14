package com.example.quickcast.room_db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.quickcast.enum_classes.SmsTypes

@Entity(
    tableName = "message_table",
    foreignKeys = [ForeignKey(
        entity = Site::class,
        parentColumns = ["id"],
        childColumns = ["siteId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["siteId"])]
)
data class Message(
    @PrimaryKey(autoGenerate = true)
    val msgId: Int = 0,
    val siteId: Int,
    val sentBy: String,
    val smsType: SmsTypes,
    val content: String
)

