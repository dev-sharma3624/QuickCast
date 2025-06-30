package com.example.quickcast.viewModels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.platform.LocalGraphicsContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickcast.data_classes.SelectedContacts
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeVM : ViewModel() {
    private val _selectedContacts = mutableStateListOf(SelectedContacts())
    val selectedContacts : List<SelectedContacts> = _selectedContacts

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
    }

     fun clearContactList(){
         viewModelScope.launch {
             delay(500)
             _selectedContacts.add(0, SelectedContacts())
             _selectedContacts.removeRange(1, _selectedContacts.size - 1)
         }
    }
}