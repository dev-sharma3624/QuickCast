package com.example.quickcast.room_db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.quickcast.room_db.converters.Converters
import com.example.quickcast.room_db.dao.MessageDao
import com.example.quickcast.room_db.dao.SiteDao
import com.example.quickcast.room_db.entities.Message
import com.example.quickcast.room_db.entities.Site

@Database(entities = [Site::class, Message::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDb : RoomDatabase() {
    abstract fun siteDao(): SiteDao
    abstract fun messageDao(): MessageDao
}