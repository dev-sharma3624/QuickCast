package com.example.quickcast.room_db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.quickcast.room_db.entities.Site
import kotlinx.coroutines.flow.Flow

@Dao
interface SiteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(site: Site): Long

    @Query("SELECT * FROM site_table")
    fun getAllSites(): Flow<List<Site>>

    @Query("UPDATE site_table set hasUnreadMessage = :b where id == :siteId")
    suspend fun changeUnreadMessageStatus(siteId : Long, b : Boolean)

}
