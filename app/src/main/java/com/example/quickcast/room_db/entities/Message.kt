package com.example.quickcast.room_db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.quickcast.enum_classes.SmsTypes

/**
 * [Message] is the room table that will store all the messages sent or received on the device.
 * */

@Entity(
    tableName = "message_table",
    foreignKeys = [
        ForeignKey(
        entity = Site::class,
        parentColumns = ["id"],
        childColumns = ["siteId"],
        onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TaskContentKeys::class,
            parentColumns = ["formatId"],
            childColumns = ["formatId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["siteId"])]
)
data class Message(
    @PrimaryKey(autoGenerate = true)
    val msgId: Long = 0,
    val siteId: Long,
    val formatId: Long?,
    val sentBy: String,
    val smsType: SmsTypes,
    val content: String
)

