package com.example.quickcast.room_db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "task_content_keys_table",
    foreignKeys = [ForeignKey(
        entity = Site::class,
        parentColumns = ["id"],
        childColumns = ["siteId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class TaskContentKeys(
    @PrimaryKey(autoGenerate = false)
    val formatId: Int,
    val siteId: Int,
    val format: List<String>
)
