package com.example.quickcast.room_db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quickcast.room_db.dto.MessageDTO
import com.example.quickcast.room_db.entities.Message

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: Message): Long

    @Query("""
        SELECT msgId, sentBy, smsType, content
        FROM message_table
        WHERE siteId = :siteId
    """)
    suspend fun getMessagesDTOForSite(siteId: Int): List<MessageDTO>
}
