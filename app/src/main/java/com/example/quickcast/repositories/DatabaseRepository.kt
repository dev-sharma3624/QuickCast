package com.example.quickcast.repositories

import com.example.quickcast.data_classes.SmsFormats.SiteInvite
import com.example.quickcast.enum_classes.SmsTypes
import com.example.quickcast.room_db.dao.MessageDao
import com.example.quickcast.room_db.dao.SiteDao
import com.example.quickcast.room_db.dto.MessageDTO
import com.example.quickcast.room_db.entities.Message
import com.example.quickcast.room_db.entities.Site
import kotlinx.coroutines.flow.Flow

class DatabaseRepository(
    private val siteDao: SiteDao,
    private val messageDao: MessageDao
) {

    fun getMessageList(siteId : Long) : Flow<List<MessageDTO>> =
        messageDao.getMessagesDTOForSite(siteId)

    suspend fun addSite(siteInvite: SiteInvite, showUnreadStatus : Boolean){
        siteDao.insert(
            Site(
                id = siteInvite.ts,
                name = siteInvite.n,
                contactsList = siteInvite.l ?: emptyList(),
                hasUnreadMessage = showUnreadStatus
            )
        )
    }

    suspend fun invitationResponse(siteId: Long, b : Boolean, phoneNumber : String, contentString: String){
        messageDao.insert(Message(
            siteId = siteId,
            formatId = null,
            sentBy = phoneNumber,
            smsType = SmsTypes.INVITATION_RESPONSE,
            content = contentString
        ))
        siteDao.changeUnreadMessageStatus(siteId, b)
    }

    suspend fun getSiteList() = siteDao.getAllSites()

}