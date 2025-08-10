package com.example.quickcast.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickcast.repositories.DatabaseRepository
import com.example.quickcast.room_db.dto.MessageDTO
import com.example.quickcast.room_db.entities.Site
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SiteScreenVM(
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    lateinit var siteList : Flow<List<Site>>

    lateinit var messageList : Flow<List<MessageDTO>>

    var site : Site? = null

    init {
        viewModelScope.launch {
            siteList = databaseRepository.getSiteList()
        }
    }

    fun loadMessageList(s: Site){
        site = s
        viewModelScope.launch {
            messageList = databaseRepository.getMessageList(s.id)
        }
    }

}