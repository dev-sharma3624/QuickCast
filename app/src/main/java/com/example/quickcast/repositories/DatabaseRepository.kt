package com.example.quickcast.repositories

import android.util.Log
import com.example.quickcast.data_classes.SmsFormats.CreateTask
import com.example.quickcast.data_classes.SmsFormats.SiteInvite
import com.example.quickcast.data_classes.SmsFormats.TaskUpdate
import com.example.quickcast.enum_classes.SmsTypes
import com.example.quickcast.room_db.dao.MessageDao
import com.example.quickcast.room_db.dao.MsgFormatDao
import com.example.quickcast.room_db.dao.SiteDao
import com.example.quickcast.room_db.dto.MessageDTO
import com.example.quickcast.room_db.entities.Message
import com.example.quickcast.room_db.entities.Site
import com.example.quickcast.room_db.entities.TaskContentKeys
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow

class DatabaseRepository(
    private val siteDao: SiteDao,
    private val messageDao: MessageDao,
    private val formatDao: MsgFormatDao
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
        val contactsList = siteDao.fetchContactsListFromSiteId(siteId)

        if(contactsList.isEmpty()){
            siteDao.updateSiteContactList(siteId, listOf(phoneNumber))
            siteDao.updateSiteUnreadStatus(siteId, b)
        }else{

            val newList = mutableListOf<String>()
            contactsList.forEach {
                newList.add(it)
            }
            newList.add(phoneNumber)
            siteDao.updateSiteContactList(siteId, newList)
            siteDao.updateSiteUnreadStatus(siteId, b)
        }
    }

    suspend fun messagePropertyAddition(siteId: Long, taskString: String, phoneNumber: String){

        val gson = Gson()

        val taskObject : CreateTask = gson.fromJson(taskString, CreateTask::class.java)

        Log.d("taskObject", taskString)

        val formatId = formatDao.insertFormat(TaskContentKeys(
            siteId = siteId,
            taskName = taskObject.taskName,
            format = gson.toJson(taskObject.l)
        ))

        messageDao.insert(Message(
            siteId = siteId,
            formatId = formatId,
            sentBy = phoneNumber,
            smsType = SmsTypes.CREATE_TASK,
            content = taskString
        ))
        siteDao.updateSiteUnreadStatus(siteId, true)
    }

    fun getMessageFormatsFromSiteId(siteId: Long) : Flow<List<TaskContentKeys>>{
        return formatDao.getFormatFromId(siteId)
    }

    fun getSiteList() = siteDao.getAllSites()

    suspend fun getSiteFromId(siteId : Long) = siteDao.getSiteFromId(siteId)


    suspend fun sendUpdateMessage(siteId: Long, taskUpdate: TaskUpdate, phoneNumber: String = "SELF") {
        messageDao.insert(
            Message(
                siteId = siteId,
                formatId = taskUpdate.fId,
                sentBy = phoneNumber,
                smsType = SmsTypes.TASK_UPDATE,
                content = Gson().toJson(taskUpdate)
            )
        )
    }

    suspend fun getSiteIdFromFormatId(formatId: Long): Long {
        return formatDao.getSiteIdFromFormat(formatId)
    }

    fun getMessageListFromSiteAndFormatId(siteId: Long, formatId: Long): Flow<List<MessageDTO>> {
        return messageDao.getMessageListFromSiteAndFormatId(siteId, formatId)
    }

}