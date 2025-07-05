package com.example.quickcast.data_classes

/**
 * [SelectedContacts] : data class representing a contact that has been selected
 * for adding to either a pre existing or new site/group.
 *
 * @param contact [Contact] type object with default value null which is used in animation of
 * selected contacts.
 * @param isSelected tells whether a contact is currently selected or not. Initial/default value
 * is false.
 * */
data class SelectedContacts(
    val contact: Contact? = null,
    var isSelected: Boolean = false
)
