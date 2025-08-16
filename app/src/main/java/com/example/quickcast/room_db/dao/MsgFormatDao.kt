package com.example.quickcast.room_db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.quickcast.room_db.entities.TaskContentKeys

@Dao
interface MsgFormatDao {

    @Insert
    suspend fun insertFormat(item : TaskContentKeys) : Long

}