package com.example.quickcast.services

import android.content.Context
import android.provider.ContactsContract
import com.example.quickcast.data_classes.Contact


/**
 * [ContactsService] : class containing functions that provide functionality
 * to interact with contacts data.
 * */

class ContactsService {

    /**
     * [getContactsList] : function that returns a List<[Contact] from the device.
     * */
    fun getContactsList(context: Context) : List<Contact>{
        val contacts = mutableListOf<Contact>()

        // content resolver that provides a medium to access shared resources
        // available in the device. Like contacts, media etc. Operations like query,
        // update, insert etc. can be performed over them with it.
        val resolver = context.contentResolver

        // gets the required data through resolver.query and returns a
        // cursor to the data. Cursor is like a pointer that points to the
        // first row of data as the data is in table format.
        val cursor = resolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
            ),
            null,
            null,
            "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} ASC"
        )

        // use method auto-closes cursor so that there are no memory leaks and other services
        // inside the app or device can use the resources.
        cursor?.use {
            // get indices of name and number columns.
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            // iterate through table row by row and append it to the list.
            while (it.moveToNext()){
                val name = it.getString(nameIndex)
                val number = it.getString(numberIndex)
                contacts.add(Contact(name,number))
            }
        }

        return contacts
    }
}