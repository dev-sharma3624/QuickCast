package com.example.quickcast.room_db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.quickcast.room_db.entities.TaskContentKeys
import kotlinx.coroutines.flow.Flow

@Dao
interface MsgFormatDao {

    @Insert
    suspend fun insertFormat(item : TaskContentKeys) : Long

    @Query("SELECT * FROM task_content_keys_table WHERE siteId == :siteId")
    fun getFormatFromId(siteId : Long) : Flow<List<TaskContentKeys>>

    @Query("SELECT siteId FROM task_content_keys_table WHERE formatId == :formatId")
    suspend fun getSiteIdFromFormat(formatId: Long) : Long

}