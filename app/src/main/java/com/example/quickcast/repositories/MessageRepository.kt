package com.example.quickcast.repositories

import com.example.quickcast.room_db.dao.MessageDao
import com.example.quickcast.room_db.dto.MessageDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class MessageRepository(
    private val messageDao: MessageDao
) {

    fun getMessageList(siteId : Int, coroutineScope : CoroutineScope) : StateFlow<List<MessageDTO>> =
        messageDao.getMessagesDTOForSite(siteId).stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(2000),
            initialValue = emptyList()
        )

}