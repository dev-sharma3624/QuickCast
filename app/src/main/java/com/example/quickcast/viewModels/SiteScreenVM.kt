package com.example.quickcast.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickcast.SmsUtils.SmsCreator
import com.example.quickcast.data_classes.MessageProperties
import com.example.quickcast.repositories.DatabaseRepository
import com.example.quickcast.repositories.SmsRepository
import com.example.quickcast.room_db.dto.MessageDTO
import com.example.quickcast.room_db.entities.Site
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SiteScreenVM(
    private val databaseRepository: DatabaseRepository,
    private val smsCreator: SmsCreator,
    private val smsRepository: SmsRepository
) : ViewModel() {

    lateinit var siteList : Flow<List<Site>>

    lateinit var messageList : Flow<List<MessageDTO>>

    var site : Site? = null

    var showDialog by mutableStateOf(false)
    val propertyList = mutableStateListOf<MessageProperties>()

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

    fun sendPropertyFieldMsg(){
        viewModelScope.launch(Dispatchers.IO + NonCancellable) {
            val list = propertyList.filter { it.name.isNotBlank() && (it.value != null && it.value > 0) }
            val msgPairList = smsCreator.createSmsPackageWithNumber(list, site!!)
            smsRepository.sendMessage(msgPairList)
        }
    }

}