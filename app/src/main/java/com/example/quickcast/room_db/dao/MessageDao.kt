package com.example.quickcast.room_db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quickcast.room_db.dto.MessageDTO
import com.example.quickcast.room_db.entities.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: Message): Long

    @Query("""
        SELECT msgId, sentBy, smsType, content
        FROM message_table
        WHERE siteId = :siteId
    """)
    fun getMessagesDTOForSite(siteId: Long): Flow<List<MessageDTO>>
}
