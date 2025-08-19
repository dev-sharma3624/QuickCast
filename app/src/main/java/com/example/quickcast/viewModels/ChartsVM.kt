package com.example.quickcast.viewModels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickcast.data_classes.MessageGraph
import com.example.quickcast.data_classes.SmsFormats.CreateTask
import com.example.quickcast.data_classes.SmsFormats.SendableMessageProperty
import com.example.quickcast.data_classes.SmsFormats.SmsPackage
import com.example.quickcast.data_classes.SmsFormats.TaskUpdate
import com.example.quickcast.enum_classes.SmsTypes
import com.example.quickcast.repositories.DatabaseRepository
import com.example.quickcast.room_db.entities.Site
import com.example.quickcast.room_db.entities.TaskContentKeys
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChartsVM(
    private val databaseRepository: DatabaseRepository,
) : ViewModel() {

    val tag = "ChartsVM"

    private val _initialSetup = mutableStateOf(false)
    val initialSetup : State<Boolean> = _initialSetup

    lateinit var siteList : Flow<List<Site>>
    lateinit var formatList : Flow<List<TaskContentKeys>>

    lateinit var messageList : Flow<List<MessageGraph>>

    init {
        Log.d(tag, "init block")
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(tag, "init bloc inside coroutine scope")
            siteList = databaseRepository.getSiteList()
            Log.d(tag, "init bloc siteList done")
            formatList = databaseRepository.getMessageFormatsFromSiteId(siteList.first().get(0).id)
            Log.d(tag, "init bloc formatList done")
            val firstObj = formatList.first().get(0)
            Log.d(tag, "init bloc firstObj done")
            loadMessages(
                firstObj.formatId,
                firstObj.siteId
            )
            Log.d(tag, "init bloc loadMessages done")
            withContext(Dispatchers.Main) {
                _initialSetup.value = true
                Log.d(tag, "Value updated on Main thread: ${initialSetup.value}")
            }
        }
    }

    fun loadFormats(siteId: Long){
        viewModelScope.launch(Dispatchers.IO) {
            formatList = databaseRepository.getMessageFormatsFromSiteId(siteId)
            Log.d(tag, "${formatList.first()}")
        }
    }

    fun loadMessages(formatId : Long, siteId: Long){
        val gson = Gson()
        viewModelScope.launch(Dispatchers.IO) {
            messageList = databaseRepository.getMessageListFromSiteAndFormatId(siteId, formatId)
                .map { messageDTOS ->
                    messageDTOS.map {

                        if(it.smsType == SmsTypes.CREATE_TASK){

                            val createTask : CreateTask = gson.fromJson(
                                it.content,
                                CreateTask::class.java
                            )

                            MessageGraph(
                                msgId = it.msgId,
                                sentBy = it.sentBy,
                                smsType = it.smsType,
                                content = createTask.l
                            )
                        }else{

                            val taskUpdate : TaskUpdate = gson.fromJson(
                                it.content,
                                TaskUpdate::class.java
                            )

                            MessageGraph(
                                msgId = it.msgId,
                                sentBy = it.sentBy,
                                smsType = it.smsType,
                                content = taskUpdate.l
                            )
                        }
                    }
                }
        }
    }

    fun createSingleCountFieldForMultipleContacts(){
        viewModelScope.launch {
            val l = messageList.first()

        }
    }


}