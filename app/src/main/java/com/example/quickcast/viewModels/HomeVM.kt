package com.example.quickcast.viewModels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickcast.SmsUtils.SmsSenderWorker
import com.example.quickcast.data_classes.MessageProperties
import com.example.quickcast.data_classes.SelectedContacts
import com.example.quickcast.data_classes.SmsFormats.SiteInvite
import com.example.quickcast.data_classes.SmsFormats.SmsPackage
import com.example.quickcast.enum_classes.SmsTypes
import com.example.quickcast.repositories.DatabaseRepository
import com.example.quickcast.repositories.SmsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar

class HomeVM(
    private val smsRepository: SmsRepository,
    private val databaseRepository: DatabaseRepository
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

    var showDialog by mutableStateOf(false)
    val propertyList = mutableStateListOf<MessageProperties>()


    /**
     * [createSmsPackageWithNumber] creates a list<[SmsPackage] from [_selectedContacts]
     * */
    private fun createSmsPackageWithNumber() : List<Pair<String, SmsPackage>>{
        val smsList = mutableListOf<Pair<String, SmsPackage>>()

        selectedContacts.forEach {
            if(it.contact != null){
                smsList.add(
                    Pair(
                        it.contact.number,
                        SmsPackage(
                            type = SmsTypes.SITE_INVITE,
                            message = SiteInvite(
                                ts = Calendar.getInstance().timeInMillis,
                                n = siteName.value,
                                l = null
                            )
                        )
                    )
                )
            }
        }

        return smsList
    }


    /**
     *  [sendMessage] sends List<[SmsPackage]> to [SmsSenderWorker] for
     *  sending messages..
     * */
    fun sendMessage() {

        // get list<SmsPackage> from _selectedContacts
        val smsList = createSmsPackageWithNumber()

        viewModelScope.launch(Dispatchers.IO + NonCancellable) {
            smsRepository.sendMessage(smsList)
        }

        // controls animation between circularProgressIndicator and content
        // on AddSiteScreenSecond
        _isSmsProcessActive.value = false

        // showing snack bar message to user
        _snackBarMessage.value = "Invite sent successfully."

    }

    fun acceptInvitation() {
        viewModelScope.launch {
            siteInviteObject.value?.let {
                databaseRepository.addSite(it)
            }
            isBottomSheetActive.value = false
        }
    }

    /** [setIsSmsProcessActive] animates AddSiteScreenSecond from content to CircularProgressIndicator*/
    fun setIsSmsProcessActive() {
        _isSmsProcessActive.value = true
    }

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
}