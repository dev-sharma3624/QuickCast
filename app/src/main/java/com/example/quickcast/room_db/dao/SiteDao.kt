package com.example.quickcast.room_db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quickcast.room_db.entities.Site
import kotlinx.coroutines.flow.Flow

@Dao
interface SiteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(site: Site): Long

    @Query("SELECT * FROM site_table")
    fun getAllSites(): Flow<List<Site>>

    @Query("UPDATE site_table set contactsList = :contactList where id == :siteId")
    suspend fun updateSiteContactList(siteId : Long, contactList: List<String>)

    @Query("UPDATE site_table SET hasUnreadMessage = :b WHERE id == :siteId")
    suspend fun updateSiteUnreadStatus(siteId: Long, b : Boolean)

    @Query("SELECT contactsList from site_table where id == :siteId")
    suspend fun fetchContactsListFromSiteId(siteId: Long) : List<String>

    @Query("SELECT * FROM site_table WHERE id == :siteId")
    suspend fun getSiteFromId(siteId: Long): Site

}
