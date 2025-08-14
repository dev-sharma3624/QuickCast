package com.example.quickcast.room_db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


/**
 * [TaskContentKeys] is the room table that stores the format/keys of a newly created task or a regular update for which
 * the members of the group will send values as updates.
 * */

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
    @PrimaryKey(autoGenerate = true)
    val formatId: Int = 0,
    val siteId: Long,
    val format: List<String>
)
