package com.example.quickcast.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickcast.repositories.DatabaseRepository
import com.example.quickcast.room_db.dto.MessageDTO
import kotlinx.coroutines.flow.StateFlow

class SiteScreenVM(
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    var messageList : StateFlow<List<MessageDTO>> =
        databaseRepository.getMessageList(TODO(), viewModelScope)

}