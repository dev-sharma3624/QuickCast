package com.example.quickcast.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickcast.repositories.MessageRepository
import com.example.quickcast.room_db.dao.MessageDao
import com.example.quickcast.room_db.dto.MessageDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SiteScreenVM(
    private val messageRepository: MessageRepository
) : ViewModel() {

    var messageList : StateFlow<List<MessageDTO>> =
        messageRepository.getMessageList(TODO(), viewModelScope)

}