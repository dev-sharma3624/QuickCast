package com.example.quickcast.viewModels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalGraphicsContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.quickcast.SmsUtils.SmsSenderWorker
import com.example.quickcast.data_classes.SelectedContacts
import com.example.quickcast.data_classes.SmsFormats.SiteInvite
import com.example.quickcast.data_classes.SmsFormats.SmsPackage
import com.example.quickcast.enum_classes.SmsTypes
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeVM : ViewModel() {
    private val _selectedContacts = mutableStateListOf(SelectedContacts())
    val selectedContacts : List<SelectedContacts> = _selectedContacts

    val siteName = mutableStateOf("")

    private val _isSmsProcessActive = mutableStateOf(false)
    val isSmsProcessActive : State<Boolean> = _isSmsProcessActive

    fun selectContact(c : SelectedContacts){
        _selectedContacts.add(_selectedContacts.indexOf(SelectedContacts()), c)
        Log.d("NAMASTE", "log of select : $selectedContacts")
    }

    fun unselectContact(){
        //removing entries that have been unselected after selection after exit animation
        if(_selectedContacts.isNotEmpty()){
            _selectedContacts.removeIf { it.contact != null && !it.isSelected }
        }

        //removing more than one redundant entries
        if(_selectedContacts[0] == SelectedContacts() && selectedContacts.size > 1){
            _selectedContacts.removeRange(1, _selectedContacts.size-1)
        }

        Log.d("NAMASTE", "log of unselect : $selectedContacts")
    }

    fun updateContactsList(selectedContacts: SelectedContacts, value: Boolean){
        _selectedContacts[_selectedContacts.indexOf(selectedContacts)].isSelected = value
        _selectedContacts.add(SelectedContacts())
        viewModelScope.launch {
            delay(500)
            unselectContact()
        }
    }

     fun clearContactList(){
         viewModelScope.launch {
             delay(500)
             _selectedContacts.add(0, SelectedContacts())
             _selectedContacts.removeRange(1, _selectedContacts.size - 1)
         }
    }

    fun clearSiteName() {
        viewModelScope.launch {
            delay(100)
            siteName.value = ""
        }
    }

    fun scheduleSmsSending(context: Context) {

        val smsList = createSmsPackage()

        val gson = Gson()
        val jsonMsgList = gson.toJson(smsList)

        val inputData = workDataOf(
            "messageList" to jsonMsgList
        )

        val request = OneTimeWorkRequestBuilder<SmsSenderWorker>()
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(context).enqueue(request)

        _isSmsProcessActive.value = false

    }

    private fun createSmsPackage() : List<SmsPackage>{
        val smsList = mutableListOf<SmsPackage>()

        selectedContacts.forEach {
            if(it.contact != null){
                smsList.add(
                    SmsPackage(
                        type = SmsTypes.SITE_INVITE,
                        phone = it.contact.number,
                        message = SiteInvite(
                            n = siteName.value,
                            l = null
                        )
                    )
                )
            }
        }

        return smsList
    }

    fun setIsSmsProcessActive() {
        _isSmsProcessActive.value = true
    }
}