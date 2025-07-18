package com.example.quickcast.room_db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "site_table")
data class Site(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val name: String,
    val contactsList: List<String>
)

