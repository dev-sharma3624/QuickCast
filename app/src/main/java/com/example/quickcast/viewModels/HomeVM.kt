package com.example.quickcast.viewModels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
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
import com.example.quickcast.room_db.dao.MessageDao
import com.example.quickcast.room_db.dao.SiteDao
import com.example.quickcast.room_db.entities.Site
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar

class HomeVM(
    private val siteDao: SiteDao,
    private val messageDao: MessageDao
) : ViewModel() {
    // list of contacts selected for creation of a new site/group
    private val _selectedContacts = mutableStateListOf(SelectedContacts())
    val selectedContacts : List<SelectedContacts> = _selectedContacts

    // site/group name for a new site.
    val siteName = mutableStateOf("")

    // controls animation between CircularProgressIndicator and
    // content while invites are being sent.
    private val _isSmsProcessActive = mutableStateOf(false)
    val isSmsProcessActive : State<Boolean> = _isSmsProcessActive

    // controls bottom sheet layout
    val isBottomSheetActive = mutableStateOf(false)

    // variable containing snack bar message that needs to be displayed if any
    private val _snackBarMessage = mutableStateOf("")
    val snackBarMessage : State<String> = _snackBarMessage

    val siteInviteObject = mutableStateOf<SiteInvite?>(null)

    /**[selectContact] invoked when an unselected contact is selected.*/
    fun selectContact(c : SelectedContacts){
        _selectedContacts.add(_selectedContacts.indexOf(SelectedContacts()), c)
        Log.d("NAMASTE", "log of select : $selectedContacts")
    }

    /**[sanitizeContactList] removes contacts from list which either have been unselected after selection
     * or there exists more than one empty elements.*/
    fun sanitizeContactList(){
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

    /**[unselectContacts] changes isSelected parameter value for elements to false.. Hence, it
     * unselects contacts.*/
    fun unselectContacts(selectedContacts: SelectedContacts, value: Boolean){
        _selectedContacts[_selectedContacts.indexOf(selectedContacts)].isSelected = value
        _selectedContacts.add(SelectedContacts())
        viewModelScope.launch {
            delay(500)
            sanitizeContactList()
        }
    }

    /**[clearContactList] removes all elements from [_selectedContacts] in case of back-navigation.*/
     fun clearContactList(){
         viewModelScope.launch {
             delay(500)
             _selectedContacts.add(0, SelectedContacts())
             _selectedContacts.removeRange(1, _selectedContacts.size - 1)
         }
    }

    /**[clearSiteName] sets [siteName] to empty string in case of back-navigation.*/
    fun clearSiteName() {
        viewModelScope.launch {
            delay(100)
            siteName.value = ""
        }
    }

    /**
     *  [scheduleSmsSending] sends List<[SmsPackage]> to [SmsSenderWorker] for
     *  sending messages..
     * */
    fun scheduleSmsSending(context: Context) {

        // get list<SmsPackage> from _selectedContacts
        val smsList = createSmsPackage()

        val gson = Gson()

        //converts smsList to json format
        val jsonMsgList = gson.toJson(smsList)

        //converts jsonMsgList as Data type object
        val inputData = workDataOf(
            "messageList" to jsonMsgList
        )

        //generates one time work request with input data for SmsSenderWorker
        val request = OneTimeWorkRequestBuilder<SmsSenderWorker>()
            .setInputData(inputData)
            .build()

        //enqueues request for background processing
        WorkManager.getInstance(context).enqueue(request)

        // controls animation between circularProgressIndicator and content
        // on AddSiteScreenSecond
        _isSmsProcessActive.value = false

        // showing snack bar message to user
        _snackBarMessage.value = "Invite sent successfully."

    }

    /**
     * [createSmsPackage] creates a list<[SmsPackage] from [_selectedContacts]
     * */
    private fun createSmsPackage() : List<SmsPackage>{
        val smsList = mutableListOf<SmsPackage>()

        selectedContacts.forEach {
            if(it.contact != null){
                smsList.add(
                    SmsPackage(
                        type = SmsTypes.SITE_INVITE,
                        phone = it.contact.number,
                        message = SiteInvite(
                            ts = Calendar.getInstance().timeInMillis,
                            n = siteName.value,
                            l = null
                        )
                    )
                )
            }
        }

        return smsList
    }

    /** [setIsSmsProcessActive] animates AddSiteScreenSecond from content to CircularProgressIndicator*/
    fun setIsSmsProcessActive() {
        _isSmsProcessActive.value = true
    }

    fun acceptInvitation() {
        viewModelScope.launch {
            siteInviteObject.value?.let {
                siteDao.insert(
                    Site(
                        id = it.ts,
                        name = it.n,
                        contactsList = it.l ?: emptyList()
                    )
                )
            }
            isBottomSheetActive.value = false
        }
    }
}