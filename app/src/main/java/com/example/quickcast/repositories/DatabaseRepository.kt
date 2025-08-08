package com.example.quickcast.repositories

import com.example.quickcast.data_classes.SmsFormats.SiteInvite
import com.example.quickcast.room_db.dao.MessageDao
import com.example.quickcast.room_db.dao.SiteDao
import com.example.quickcast.room_db.dto.MessageDTO
import com.example.quickcast.room_db.entities.Site
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class DatabaseRepository(
    private val siteDao: SiteDao,
    private val messageDao: MessageDao
) {

    fun getMessageList(siteId : Int) : Flow<List<MessageDTO>> =
        messageDao.getMessagesDTOForSite(siteId)

    suspend fun addSite(siteInvite: SiteInvite){
        siteDao.insert(
            Site(
                id = siteInvite.ts,
                name = siteInvite.n,
                contactsList = siteInvite.l ?: emptyList()
            )
        )
    }

}