package com.example.quickcast.services

import android.content.Context
import android.provider.ContactsContract
import com.example.quickcast.data_classes.Contact

class ContactsService {
    fun getContactsList(context: Context) : List<Contact>{
        val contacts = mutableListOf<Contact>()

        val resolver = context.contentResolver

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

        cursor?.use {
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (it.moveToNext()){
                val name = it.getString(nameIndex)
                val number = it.getString(numberIndex)
                contacts.add(Contact(name,number))
            }
        }

        return contacts
    }
}