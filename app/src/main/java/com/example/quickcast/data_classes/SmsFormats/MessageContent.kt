package com.example.quickcast.data_classes.SmsFormats

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * [MessageContent] works as the base class for all message types.
 * */

@Parcelize
sealed class MessageContent : Parcelable


/**
 * [SiteInvite] : data class representing the object that will be sent
 * for sending a invitation to someone to join a preexisting or new site/group.
 *
 * @param n represent site name
 * @param l represents the list of contacts that exist in a site/group.
 * It will be null if the site/group is new.
 * */

@Parcelize
data class SiteInvite (
    val n : String, //site name
    val l : List<String>? //list of all other contacts
) : MessageContent()
