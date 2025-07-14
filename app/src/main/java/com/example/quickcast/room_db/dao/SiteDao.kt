package com.example.quickcast.room_db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quickcast.room_db.entities.Site

@Dao
interface SiteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(site: Site): Long

    @Query("SELECT * FROM site_table")
    suspend fun getAllSites(): List<Site>
}
